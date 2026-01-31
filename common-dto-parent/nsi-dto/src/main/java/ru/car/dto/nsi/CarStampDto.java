package ru.car.dto.nsi;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CarStampDto {
    private Long id;
    private String code;
    private String name;
    private String country;
    private Boolean isActive;
    private LocalDateTime createdAt;
}