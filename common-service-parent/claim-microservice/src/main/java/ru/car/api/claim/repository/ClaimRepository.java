package ru.car.api.claim.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.car.entity.claim.ClaimEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClaimRepository extends JpaRepository<ClaimEntity, Long> {
    List<ClaimEntity> findByClientId(Long clientId);
    List<ClaimEntity> findByMasterId(Long masterId);
    List<ClaimEntity> findByStatus(String status);
    List<ClaimEntity> findByIsPaid(Boolean isPaid);
    boolean existsByClaimNumber(String claimNumber);


}
