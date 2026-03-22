package ru.car.dto.nsi;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CarModelDto {
    private Long id;
    private String code;
    private String name;
    private Long stampId;
    private String stampName;
    private String productionYears;
    private VehicleType vehicleType;
    private Boolean isActive;
    private LocalDateTime createdAt;
}