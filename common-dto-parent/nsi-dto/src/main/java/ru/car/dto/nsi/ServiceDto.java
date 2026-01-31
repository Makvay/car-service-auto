package ru.car.dto.nsi;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ServiceDto {
    private Long id;
    private String code;
    private String name;
    private String description;
    private Double standardPrice;
    private Integer standardDurationMin;
    private Boolean isActive;
    private LocalDateTime createdAt;
}