package ru.car.api.master.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.car.entity.master.MasterSpecializationEntity;

import java.util.List;

@Repository
public interface MasterSpecializationRepository extends JpaRepository<MasterSpecializationEntity, Long> {
    List<MasterSpecializationEntity> findByMasterId(Long masterId);
    List<MasterSpecializationEntity> findBySpecializationIgnoreCase(String specialization);
    void deleteByMasterId(Long masterId);
}
