package ru.car.dto.nsi;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdatePartCategoryRequest {
    @Size(max = 50)
    private String code;

    @Size(max = 200)
    private String name;

    @Size(max = 1000)
    private String description;

    private Long parentId;

    private Boolean parentIdIsNull; // Для установки parentId в null

    private Boolean isActive;
}