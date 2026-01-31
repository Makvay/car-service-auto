package ru.car.dto.warehouse;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class CreatePartRequest {
    @NotBlank(message = "Артикул обязателен")
    private String sku;

    @NotBlank(message = "Название обязательно")
    private String name;

    @NotNull(message = "Категория обязательна")
    private Long categoryId;

    @NotNull(message = "Цена продажи обязательна")
    private Double unitPrice;

    @NotNull(message = "Себестоимость обязательна")
    private Double costPrice;

    private String description;
    private String brand;
    private String measurementUnit;
}