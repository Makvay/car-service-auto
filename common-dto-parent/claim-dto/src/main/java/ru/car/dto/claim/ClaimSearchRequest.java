package ru.car.dto.claim;

import lombok.Data;

import java.util.List;

@Data
public class ClaimSearchRequest {
    private Long clientId;
    private Long vehicleId;
    private Long masterId;
    private List<ClaimStatus> statuses;
    private ClaimPriority priority;



}
