package ru.car.dto.nsi;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreatePartCategoryRequest {

    @NotBlank
    @Size(max = 50)
    private String code;

    @NotBlank
    @Size(max = 200)
    private String name;

    @Size(max = 1000)
    private String description;

    private Long parentId;
}