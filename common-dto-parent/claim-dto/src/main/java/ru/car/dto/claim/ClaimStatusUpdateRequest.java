package ru.car.dto.claim;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class ClaimStatusUpdateRequest {
    @NotNull(message = "Status is required")
    private ClaimStatus status;
    private String comment;
}

