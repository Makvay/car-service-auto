package ru.car.dto.nsi;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateServiceRequest {

    @NotBlank(message = "Код услуги обязателен")
    private String code;

    @NotBlank(message = "Название услуги обязательно")
    private String name;

    private String description;

    @NotNull(message = "Стандартная цена обязательна")
    @Positive(message = "Цена должна быть положительной")
    private Double standardPrice;

    @NotNull(message = "Стандартная длительность обязательна")
    @Positive(message = "Длительность должна быть положительной")
    private Integer standardDurationMin;

    private Boolean isActive = true;
}