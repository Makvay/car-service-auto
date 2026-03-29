package ru.car.api.warehouse.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.car.api.warehouse.mapper.SupplyMapper;
import ru.car.api.warehouse.repository.SupplyRepository;
import ru.car.api.warehouse.service.SupplyService;
import ru.car.dto.warehouse.SupplyDto;
import ru.car.entity.warehouse.SupplyEntity;

@Service
@Slf4j
@RequiredArgsConstructor
public class SupplyServiceImpl implements SupplyService {

    private final SupplyRepository supplyRepository;
    private final SupplyMapper supplyMapper;

    @Transactional
    @Override
    public SupplyDto updateStatus(Long id, String status) {
        log.info("Обновить статус  {} ", status);

        SupplyEntity supply = supplyRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Not found"));

        supply.setStatus(status);
        SupplyEntity saved = supplyRepository.save(supply);

        log.info("Статус обновлен: {}: {}", id, status);
        return supplyMapper.toDto(saved);
    }

    @Override
    public SupplyDto getByStatus(Long id) {
        log.info("Получить поставку с ID: {}", id);

        SupplyEntity supply = supplyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        return supplyMapper.toDto(supply);
    }

}
