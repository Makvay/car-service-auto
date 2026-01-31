package ru.car.dto.warehouse;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PartDto {
    private Long id;
    private String sku;
    private String name;
    private String description;
    private Long categoryId;
    private String brand;
    private Double unitPrice;
    private Double costPrice;
    private String measurementUnit;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}