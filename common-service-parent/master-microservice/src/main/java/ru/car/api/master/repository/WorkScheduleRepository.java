package ru.car.api.master.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.car.entity.master.WorkScheduleEntity;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WorkScheduleRepository extends JpaRepository<WorkScheduleEntity, Long> {
    List<WorkScheduleEntity> findByMasterId(Long masterId);
    List<WorkScheduleEntity> findByMasterIdAndDateBetween(Long masterId, LocalDate startDate, LocalDate endDate);
}
