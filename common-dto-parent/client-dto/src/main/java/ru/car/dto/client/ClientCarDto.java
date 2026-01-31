package ru.car.dto.client;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ClientCarDto {
    private Long id;
    private Long clientId;
    private String vin;
    private String brand;
    private String model;
    private Integer year;
    private String licensePlate;
    private Integer mileage;
    private LocalDateTime createdAt;
}