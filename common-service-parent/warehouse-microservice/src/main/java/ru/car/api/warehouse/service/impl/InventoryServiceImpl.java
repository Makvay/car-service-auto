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
        List<InventoryEntity> inventories = inventoryRepository.findByPartId(partId);
        return inventoryMapper.toDtoList(inventories);
    }

    @Transactional
    @Override
    public InventoryDto updateQuantity(Long id, Integer quantity) {
        InventoryEntity inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory row not found: " + id));
        inventory.setQuantity(quantity);
        return inventoryMapper.toDto(inventoryRepository.save(inventory));
    }

    @Transactional
    @Override
    public void deductInventory(DeductRequest request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new RuntimeException("No items provided for deduction");
        }

        // Pre-check all rows so we either deduct everything or fail.
        for (DeductRequest.DeductItem item : request.getItems()) {
            if (item.getPartId() == null || item.getQuantity() == null || item.getQuantity() <= 0) {
                throw new RuntimeException("Invalid deduction item: partId and positive quantity are required");
            }
            List<InventoryEntity> inventories = inventoryRepository.findWithLockByPartIdOrderByIdAsc(item.getPartId());
            if (inventories.isEmpty()) {
                throw new RuntimeException("Part " + item.getPartId() + " not found in inventory");
            }
            int available = inventories.stream()
                    .mapToInt(i -> Math.max(0, safe(i.getQuantity()) - safe(i.getReservedQuantity())))
                    .sum();
            if (available < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock for part " + item.getPartId()
                        + ": required=" + item.getQuantity() + ", available=" + available);
            }
        }

        for (DeductRequest.DeductItem item : request.getItems()) {
            int required = item.getQuantity();
            List<InventoryEntity> inventories = inventoryRepository.findWithLockByPartIdOrderByIdAsc(item.getPartId());
            for (InventoryEntity inventory : inventories) {
                if (required == 0) {
                    break;
                }
                int quantity = safe(inventory.getQuantity());
                int reserved = safe(inventory.getReservedQuantity());
                int available = Math.max(0, quantity - reserved);
                if (available == 0) {
                    continue;
                }
                int deduct = Math.min(available, required);
                inventory.setQuantity(quantity - deduct);
                inventoryRepository.save(inventory);
                required -= deduct;
            }

            if (required != 0) {
                throw new RuntimeException("Deduction invariant failed for part " + item.getPartId());
            }
        }
    }

    private int safe(Integer value) {
        return value == null ? 0 : value;
    }
}
