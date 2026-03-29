package ru.car.api.warehouse.service;

import org.springframework.stereotype.Service;
import ru.car.dto.warehouse.InventoryDto;

import java.util.List;


public interface InventoryService {

    List<InventoryDto> getByPartId(Long partId);
    InventoryDto updateQuantity(Long id, Integer quantity);

}
