package ru.car.api.claim.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.car.entity.claim.ClaimWorkItemEntity;

import java.util.List;

@Repository
public interface ClaimWorkItemRepository extends JpaRepository<ClaimWorkItemEntity, Long> {
    List<ClaimWorkItemEntity> findByClaimId(Long claimId);
}
