package ru.car.dto.client;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Data
public class CreateClientCarRequest {
    @NotNull(message = "ID клиента обязательно")
    private Long clientId;

    @NotBlank(message = "VIN номер обязателен")
    @Pattern(regexp = "^[A-HJ-NPR-Z0-9]{17}$", message = "Неверный формат VIN")
    private String vin;

    private String brand;
    private String model;
    private Integer year;
    private String licensePlate;
    private Integer mileage;
}