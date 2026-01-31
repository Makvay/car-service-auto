package ru.car.dto.warehouse;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SupplyItemDto {
    private Long id;
    private Long supplyId;
    private Long partId;
    private Integer quantity;
    private Double unitCost;
    private Double totalCost;
    private LocalDateTime createdAt;
}