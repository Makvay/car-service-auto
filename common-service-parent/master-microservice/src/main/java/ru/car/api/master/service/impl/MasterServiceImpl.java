package ru.car.api.master.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.car.api.master.mapper.MasterMapper;
import ru.car.api.master.repository.MasterRepository;
import ru.car.api.master.repository.MasterSpecializationRepository;
import ru.car.api.master.service.MasterService;
import ru.car.dto.master.CreateMasterRequest;
import ru.car.dto.master.MasterDto;
import ru.car.entity.master.MasterEntity;
import ru.car.entity.master.MasterQualification;
import ru.car.entity.master.MasterSpecializationEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MasterServiceImpl implements MasterService {

    private final MasterRepository masterRepository;
    private final MasterSpecializationRepository specializationRepository;
    private final MasterMapper masterMapper;

    @Override
    @Transactional
    public MasterDto createMaster(CreateMasterRequest request) {
        log.info("Creating new master with employee code: {}", request.getEmployeeCode());

        if (masterRepository.existsByEmployeeCode(request.getEmployeeCode())) {
            throw new IllegalArgumentException("Master with this employee code already exists");
        }
        if (masterRepository.existsByPhone(request.getPhone())) {
            throw new IllegalArgumentException("Master with this phone already exists");
        }
        if (masterRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Master with this email already exists");
        }

        MasterEntity entity = masterMapper.toEntity(request);

        if (request.getQualificationLevel() != null && !request.getQualificationLevel().isEmpty()) {
            entity.setQualificationLevel(MasterQualification.valueOf(request.getQualificationLevel()));
        }

        entity.setIsActive(true);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setHireDate(request.getHireDate());

        MasterEntity saved = masterRepository.save(entity);
        
        // Сохраняем специализации
        if (request.getSpecializations() != null && !request.getSpecializations().isEmpty()) {
            saveSpecializations(saved.getId(), request.getSpecializations());
        }
        
        log.info("Created master with id: {}", saved.getId());

        return convertToDto(saved);
    }

    @Override
    public MasterDto getMasterById(Long id) {
        log.debug("Getting master by id: {}", id);
        MasterEntity entity = masterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Master not found with id: " + id));
        return convertToDto(entity);
    }

    @Override
    public MasterDto getMasterByEmployeeCode(String employeeCode) {
        log.debug("Getting master by employee code: {}", employeeCode);
        MasterEntity entity = masterRepository.findByEmployeeCode(employeeCode)
                .orElseThrow(() -> new IllegalArgumentException("Master not found with employee code: " + employeeCode));
        return convertToDto(entity);
    }

    @Override
    public Page<MasterDto> getAllMasters(Pageable pageable) {
        log.debug("Getting all masters, page: {}", pageable.getPageNumber());
        Page<MasterEntity> entities = masterRepository.findAll(pageable);
        return entities.map(this::convertToDto);
    }

    @Override
    public Page<MasterDto> getActiveMasters(Pageable pageable) {
        log.debug("Getting active masters, page: {}", pageable.getPageNumber());
        Page<MasterEntity> entities = masterRepository.findByIsActive(true, pageable);
        return entities.map(this::convertToDto);
    }

    @Override
    public Page<MasterDto> getMastersBySpecialization(String specialization, Pageable pageable) {
        log.debug("Getting masters by specialization: {}, page: {}", specialization, pageable.getPageNumber());
        List<MasterEntity> allMasters = masterRepository.findAll();
        List<MasterEntity> filtered = allMasters.stream()
            .filter(m -> m.getSpecialization() != null && 
                m.getSpecialization().name().equalsIgnoreCase(specialization))
            .collect(Collectors.toList());
        
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), filtered.size());
        List<MasterDto> pageContent = filtered.subList(start, end).stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
        
        return new PageImpl<>(pageContent, pageable, filtered.size());
    }

    @Override
    @Transactional
    public MasterDto updateMaster(Long id, CreateMasterRequest request) {
        log.info("Updating master with id: {}", id);
        MasterEntity entity = masterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Master not found with id: " + id));

        entity.setFirstName(request.getFirstName());
        entity.setLastName(request.getLastName());
        entity.setPhone(request.getPhone());
        entity.setEmail(request.getEmail());
        if (request.getQualificationLevel() != null && !request.getQualificationLevel().isEmpty()) {
            entity.setQualificationLevel(MasterQualification.valueOf(request.getQualificationLevel()));
        }
        entity.setHourlyRate(request.getHourlyRate());
        entity.setHireDate(request.getHireDate());
        entity.setUpdatedAt(LocalDateTime.now());

        MasterEntity saved = masterRepository.save(entity);
        
        // Обновляем специализации
        if (request.getSpecializations() != null) {
            saveSpecializations(saved.getId(), request.getSpecializations());
        }
        
        log.info("Updated master with id: {}", saved.getId());

        return convertToDto(saved);
    }

    @Override
    @Transactional
    public void deactivateMaster(Long id) {
        log.info("Deactivating master with id: {}", id);
        MasterEntity entity = masterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Master not found with id: " + id));
        entity.setIsActive(false);
        entity.setUpdatedAt(LocalDateTime.now());
        masterRepository.save(entity);
        log.info("Deactivated master with id: {}", id);
    }

    @Override
    @Transactional
    public void activateMaster(Long id) {
        log.info("Activating master with id: {}", id);
        MasterEntity entity = masterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Master not found with id: " + id));
        entity.setIsActive(true);
        entity.setUpdatedAt(LocalDateTime.now());
        masterRepository.save(entity);
        log.info("Activated master with id: {}", id);
    }

    private void saveSpecializations(Long masterId, List<String> specializations) {
        specializationRepository.deleteByMasterId(masterId);
        for (String spec : specializations) {
            MasterSpecializationEntity entity = new MasterSpecializationEntity();
            entity.setMasterId(masterId);
            entity.setSpecialization(spec);
            entity.setCreatedAt(LocalDateTime.now());
            specializationRepository.save(entity);
        }
    }

    private MasterDto convertToDto(MasterEntity entity) {
        MasterDto dto = masterMapper.toDto(entity);
        
        // Use single specialization field
        if (entity.getSpecialization() != null) {
            dto.setSpecializations(List.of(entity.getSpecialization().name()));
        }
        
        if (entity.getQualificationLevel() != null) {
            dto.setQualificationLevel(entity.getQualificationLevel().name());
        }
        return dto;
    }
}