package ru.car.api.master.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.car.api.master.mapper.MasterMapper;
import ru.car.api.master.repository.MasterRepository;
import ru.car.api.master.service.MasterService;
import ru.car.dto.master.CreateMasterRequest;
import ru.car.dto.master.MasterDto;
import ru.car.entity.master.MasterEntity;
import ru.car.entity.master.MasterQualification;
import ru.car.entity.master.MasterSpecialization;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MasterServiceImpl implements MasterService {

    private final MasterRepository masterRepository;
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


        entity.setSpecialization(MasterSpecialization.valueOf(request.getSpecialization()));
        if (request.getQualificationLevel() != null && !request.getQualificationLevel().isEmpty()) {
            entity.setQualificationLevel(MasterQualification.valueOf(request.getQualificationLevel()));
        }

        entity.setIsActive(true);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setHireDate(request.getHireDate());

        MasterEntity saved = masterRepository.save(entity);
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
    public List<MasterDto> getAllMasters() {
        log.debug("Getting all masters");
        List<MasterEntity> entities = masterRepository.findAll();
        return convertToDtoList(entities);
    }

    @Override
    public List<MasterDto> getActiveMasters() {
        log.debug("Getting active masters");
        List<MasterEntity> entities = masterRepository.findByIsActive(true);
        return convertToDtoList(entities);
    }

    @Override
    public List<MasterDto> getMastersBySpecialization(String specialization) {
        log.debug("Getting masters by specialization: {}", specialization);
        List<MasterEntity> entities = masterRepository.findBySpecialization(specialization);
        return convertToDtoList(entities);
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
        entity.setSpecialization(MasterSpecialization.valueOf(request.getSpecialization()));
        if (request.getQualificationLevel() != null && !request.getQualificationLevel().isEmpty()) {
            entity.setQualificationLevel(MasterQualification.valueOf(request.getQualificationLevel()));
        }
        entity.setHourlyRate(request.getHourlyRate());
        entity.setHireDate(request.getHireDate());
        entity.setUpdatedAt(LocalDateTime.now());

        MasterEntity saved = masterRepository.save(entity);
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


    private MasterDto convertToDto(MasterEntity entity) {
        MasterDto dto = masterMapper.toDto(entity);
        dto.setSpecialization(entity.getSpecialization().name());
        if (entity.getQualificationLevel() != null) {
            dto.setQualificationLevel(entity.getQualificationLevel().name());
        }
        return dto;
    }

    private List<MasterDto> convertToDtoList(List<MasterEntity> entities) {
        List<MasterDto> dtos = masterMapper.toDtoList(entities);
        for (int i = 0; i < entities.size(); i++) {
            MasterEntity entity = entities.get(i);
            MasterDto dto = dtos.get(i);
            dto.setSpecialization(entity.getSpecialization().name());
            if (entity.getQualificationLevel() != null) {
                dto.setQualificationLevel(entity.getQualificationLevel().name());
            }
        }
        return dtos;
    }
}