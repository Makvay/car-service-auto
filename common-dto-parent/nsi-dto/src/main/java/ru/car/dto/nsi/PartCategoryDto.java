package ru.car.dto.nsi;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PartCategoryDto {
    private Long id;
    private String code;
    private String name;
    private Long parentId;
    private Boolean isActive;
    private LocalDateTime createdAt;
}