package ru.car.api.claim.service;

import ru.car.dto.claim.ClaimDto;
import ru.car.dto.claim.CreateClaimRequest;
import ru.car.dto.claim.ClaimStatusUpdateRequest;
import ru.car.dto.claim.WorkItemCreateRequest;
import ru.car.dto.claim.PartRequest;

import java.util.List;

public interface ClaimService {
    ClaimDto createClaim(CreateClaimRequest request);

    ClaimDto getClaim(Long id);

    List<ClaimDto> getClientClaims(Long clientId);

    List<ClaimDto> getAllClaims(String status, Long masterId, Boolean isPaid);

    ClaimDto updateClaimStatus(Long claimId, ClaimStatusUpdateRequest request);

    ClaimDto assignMaster(Long claimId, Long masterId);

    ClaimDto addWorkItem(Long claimId, WorkItemCreateRequest request);

    ClaimDto addPart(Long claimId, PartRequest request);

    void deleteClaim(Long id);
}