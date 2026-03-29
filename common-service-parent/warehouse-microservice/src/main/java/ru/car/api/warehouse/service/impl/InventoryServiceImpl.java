package ru.car.api.warehouse.service.impl;

import ru.car.api.warehouse.service.InventoryService;
import ru.car.dto.warehouse.InventoryDto;

import java.util.List;

public class InventoryServiceImpl implements InventoryService {
    @Override
    public List<InventoryDto> getByPartId(Long partId) {
        return List.of();
    }

    @Override
    public InventoryDto updateQuantity(Long id, Integer quantity) {
        return null;
    }
}
