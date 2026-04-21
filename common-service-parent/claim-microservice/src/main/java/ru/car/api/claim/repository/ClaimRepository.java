package ru.car.api.claim.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.car.entity.claim.ClaimEntity;
import ru.car.entity.claim.ClaimStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClaimRepository extends JpaRepository
        <ClaimEntity, Long>,
        JpaSpecificationExecutor<ClaimEntity> {
    Page<ClaimEntity> findByClientId(Long clientId, Pageable pageable);
    Page<ClaimEntity> findByMasterId(Long masterId, Pageable pageable);
    Page<ClaimEntity> findByStatus(ClaimStatus status, Pageable pageable);
    Page<ClaimEntity> findByIsPaid(Boolean isPaid, Pageable pageable);
    boolean existsByClaimNumber(String claimNumber);


}
