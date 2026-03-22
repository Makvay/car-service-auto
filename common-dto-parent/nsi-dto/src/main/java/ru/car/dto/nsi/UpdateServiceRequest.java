package ru.car.dto.nsi;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateServiceRequest {
    private String name;
    private String description;

    @Positive(message = "Цена должна быть положительной")
    private Double standardPrice;

    @Positive(message = "Длительность должна быть положительной")
    private Integer standardDurationMin;

    private Boolean isActive;
}