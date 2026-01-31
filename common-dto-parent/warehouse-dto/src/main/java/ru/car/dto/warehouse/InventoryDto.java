package ru.car.dto.warehouse;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class InventoryDto {
    private Long id;
    private Long partId;
    private Long locationId;
    private Integer quantity;
    private Integer reservedQuantity;
    private Integer minStockLevel;
    private Integer maxStockLevel;
    private String batchNumber;
    private LocalDate expirationDate;
    private LocalDateTime lastRestocked;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}