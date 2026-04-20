package ru.car.api.warehouse.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.car.api.warehouse.mapper.InventoryMapper;
import ru.car.api.warehouse.repository.InventoryRepository;
import ru.car.api.warehouse.service.InventoryService;
import ru.car.dto.warehouse.DeductRequest;
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

    @Transactional
    @Override
    public void deductInventory(DeductRequest request) {
        log.info("Списание товаров со склада: {}", request);
        
        if (request.getItems() == null || request.getItems().isEmpty()) {
            log.warn("Нет товаров для списания");
            return;
        }
        
        for (DeductRequest.DeductItem item : request.getItems()) {
            List<InventoryEntity> inventories = inventoryRepository.findByPartId(item.getPartId());
            
            if (inventories.isEmpty()) {
                log.warn("Товар с ID {} не найден на складе", item.getPartId());
                continue;
            }
            
            // Списываем с первого доступного склада
            InventoryEntity inventory = inventories.get(0);
            int newQuantity = inventory.getQuantity() - item.getQuantity();
            
            if (newQuantity < 0) {
                log.warn("Недостаточно товара {} на складе. Требуется: {}, доступно: {}", 
                    item.getPartId(), item.getQuantity(), inventory.getQuantity());
                newQuantity = 0;
            }
            
            inventory.setQuantity(newQuantity);
            inventoryRepository.save(inventory);
            log.info("Списано {} единиц товара {}. Остаток: {}", 
                item.getQuantity(), item.getPartId(), newQuantity);
        }
    }
}
