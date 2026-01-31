package ru.car.dto.claim;

import lombok.Data;
import ru.car.dto.nsi.ServiceDto;

import java.time.LocalDateTime;

@Data
public class ClaimWorkItemDto {
    private Long id;
    private ClaimDto claim;
    private ServiceDto service;
    private String description;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
    private Boolean isCompleted;
    private LocalDateTime createdAt;
}