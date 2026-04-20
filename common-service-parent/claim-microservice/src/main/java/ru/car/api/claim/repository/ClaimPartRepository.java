package ru.car.api.claim.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.car.entity.claim.ClaimPartEntity;

import java.util.List;

@Repository
public interface ClaimPartRepository extends JpaRepository<ClaimPartEntity, Long> {
    List<ClaimPartEntity> findByClaimId(Long claimId);
}