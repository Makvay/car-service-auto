package ru.car.api.claim.service;


import ru.car.dto.claim.*;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClaimService {
    ClaimDto createClaim(CreateClaimRequest request);

    ClaimDto getClaim(Long id);

    Page<ClaimDto> getClientClaims(Long clientId, Pageable pageable);

    Page<ClaimDto> getAllClaims(String status, Long masterId, Boolean isPaid, Pageable pageable);

    Page<ClaimDto> searchClaims(ClaimSearchRequest searchRequest, Pageable pageable);

    ClaimDto updateClaimStatus(Long claimId, ClaimStatusUpdateRequest request);

    ClaimDto assignMaster(Long claimId, Long masterId);

    ClaimDto addWorkItem(Long claimId, WorkItemCreateRequest request);

    ClaimDto addPart(Long claimId, PartRequest request);

    ClaimDto deletePart(Long claimId, Long partId);

    ClaimDto deleteWorkItem(Long claimId, Long workItemId);

    void deleteClaim(Long id);
}