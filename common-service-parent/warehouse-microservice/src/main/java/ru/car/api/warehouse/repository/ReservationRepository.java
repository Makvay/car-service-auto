package ru.car.api.warehouse.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.car.entity.warehouse.ReservationEntity;

import java.util.List;

/// findByClaimId() - резервации для заявки
/// findByStatus() - по статусу


public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

    List<ReservationEntity> findByClaimId(Long claimId);
    List<ReservationEntity> findByStatus(String status);


}
