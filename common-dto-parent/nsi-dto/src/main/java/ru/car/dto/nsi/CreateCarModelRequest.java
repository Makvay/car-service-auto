package ru.car.dto.nsi;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCarModelRequest {

    @NotNull(message = "ID марки обязательно")
    private Long brandId;

    @NotBlank(message = "Код модели обязателен")
    private String code;

    @NotBlank(message = "Название модели обязательно")
    private String name;
    @NotNull
    private Long stampId;
    private String productionYears;
    private VehicleType vehicleType;
    private Boolean isActive = true;
}