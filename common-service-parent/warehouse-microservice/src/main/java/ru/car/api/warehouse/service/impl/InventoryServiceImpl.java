package ru.car.api.warehouse.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.car.api.warehouse.mapper.InventoryMapper;
import ru.car.api.warehouse.repository.InventoryRepository;
import ru.car.api.warehouse.service.InventoryService;
import ru.car.dto.warehouse.InventoryDto;
import ru.car.entity.warehouse.InventoryEntity;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryMapper inventoryMapper;


    @Override
    public List<InventoryDto> getByPartId(Long partId) {
        log.info("Найти товар {}", partId);
        List<InventoryEntity> inventories = inventoryRepository.findByPartId(partId);
        return inventoryMapper.toDtoList(inventories);
    }

    @Transactional
    @Override
    public InventoryDto updateQuantity(Long id, Integer quantity) {
        log.info("Обновить количество для остатка {} на {}", id, quantity);
        InventoryEntity inventory = inventoryRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Товар не найден"));
        inventory.setQuantity(quantity);
        InventoryEntity updateEn = inventoryRepository.save(inventory);
        return inventoryMapper.toDto(updateEn);

    }
}
