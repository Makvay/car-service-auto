package ru.car.api.master.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.car.entity.master.MasterEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface MasterRepository extends JpaRepository<MasterEntity, Long> {
    Optional<MasterEntity> findByEmployeeCode(String employeeCode);
    Optional<MasterEntity> findByPhone(String phone);
    Optional<MasterEntity> findByEmail(String email);
    List<MasterEntity> findByIsActive(Boolean isActive);
    List<MasterEntity> findBySpecialization(String specialization);
    boolean existsByEmployeeCode(String employeeCode);
    boolean existsByPhone(String phone);
    boolean existsByEmail(String email);
}
