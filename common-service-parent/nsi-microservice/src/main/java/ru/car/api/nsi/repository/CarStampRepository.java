package ru.car.api.nsi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.car.entity.nsi.CarStampEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarStampRepository extends JpaRepository<CarStampEntity, Long>,
        JpaSpecificationExecutor<CarStampEntity> {
    
    Optional<CarStampEntity> findByCode(String code);
    List<CarStampEntity> findByIsActiveTrue();
    List<CarStampEntity> findByCountry(String country);
    boolean existsByCode(String code);
}
