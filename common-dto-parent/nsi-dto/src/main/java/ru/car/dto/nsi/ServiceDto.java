package ru.car.dto.nsi;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ServiceDto {
    private Long id;
    private String code;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer durationMinutes;
    private Long categoryId;
    private Boolean isActive;
    private LocalDateTime createdAt;
}