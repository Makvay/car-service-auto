package ru.car.dto.nsi;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;


@Data
public class CreateCarStampRequest {
    @NotBlank(message = "Код марки обязателен")
    private String code;

    @NotBlank(message = "Название марки обязательно")
    private String name;

    private String country;
}