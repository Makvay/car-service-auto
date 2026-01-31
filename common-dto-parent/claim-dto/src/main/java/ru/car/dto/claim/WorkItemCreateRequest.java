package ru.car.dto.claim;

import jakarta.validation.constraints.NotNull;

public class WorkItemCreateRequest {
    @NotNull(message = "Service ID is required")
    private Long serviceId;
    private String comment;
    private Integer estimatedHours;
}

