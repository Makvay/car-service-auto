package ru.car.dto.claim;

import lombok.Data;


import java.time.LocalDateTime;

@Data
public class ClaimWorkItemDto {
    private Long id;
    private ClaimDto claim;

    // Заменяем ServiceDto на простые поля
    private Long serviceId;
    private String serviceCode;
    private String serviceName;
    private String serviceDescription;

    private String comment;
    private Integer estimatedHours;
    private Integer actualHours;
    private Double hourlyRate;
    private Double totalCost;
    private String status; // PLANNED, IN_PROGRESS, COMPLETED
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}