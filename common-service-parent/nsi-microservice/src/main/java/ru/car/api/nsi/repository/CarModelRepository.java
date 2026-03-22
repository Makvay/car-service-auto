package ru.car.api.nsi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.car.entity.nsi.CarModelEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarModelRepository extends JpaRepository<CarModelEntity, Long> {

    List<CarModelEntity> findByStampId(Long stampId);

    List<CarModelEntity> findByIsActiveTrue();

    List<CarModelEntity> findByStampIdAndIsActiveTrue(Long stampId);

    Optional<CarModelEntity> findByCode(String code);

    boolean existsByCode(String code);
}