package ru.car.api.claim.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.car.entity.claim.ClaimStatusHistoryEntity;

@Repository
public interface ClaimStatusHistoryRepository extends JpaRepository<ClaimStatusHistoryEntity, Long> {
}
