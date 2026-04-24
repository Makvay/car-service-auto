package ru.car.api.claim.service.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaimEvent {
    private String eventId;
    private String occurredAt;
    private Long claimId;
    private String claimNumber;
    private String clientEmail;
    private String clientName;
    private String status;
    private String description;
}
