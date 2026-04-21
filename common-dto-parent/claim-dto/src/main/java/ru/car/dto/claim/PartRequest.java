package ru.car.dto.claim;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PartRequest {
    @NotNull(message = "Part ID is required")
    private Long partId;
    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    private Integer quantity;
    private String comment;
}
