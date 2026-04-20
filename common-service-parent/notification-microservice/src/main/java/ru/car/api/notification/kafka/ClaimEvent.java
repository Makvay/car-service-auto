package ru.car.api.notification.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaimEvent {
    private Long claimId;
    private String claimNumber;
    private String clientEmail;
    private String clientName;
    private String status;
    private String description;
}