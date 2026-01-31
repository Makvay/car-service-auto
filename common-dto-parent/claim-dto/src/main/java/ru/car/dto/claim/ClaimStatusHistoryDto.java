package ru.car.dto.claim;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ClaimStatusHistoryDto {
    private Long id;
    private Long claimId;
    private String oldStatus;
    private String newStatus;
    private Long changedBy;
    private String notes;
    private LocalDateTime createdAt;
}