package ru.car.dto.nsi;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CarModelDto {
    private Long id;
    private Long brandId;
    private String code;
    private String name;
    private String productionYears;
    private String vehicleType;
    private Boolean isActive;
    private LocalDateTime createdAt;
}