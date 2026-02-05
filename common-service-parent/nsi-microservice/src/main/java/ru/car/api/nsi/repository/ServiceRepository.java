package ru.car.api.nsi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.car.entity.nsi.ServiceEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long>,
        JpaSpecificationExecutor<ServiceEntity> {
    Optional<ServiceEntity> findByCode(String code);
    List<ServiceEntity> findByIsActiveTrue();
    List<ServiceEntity> findByStandardPriceBetween(Double minPrice, Double maxPrice);
    boolean existsByCode(String code);
}
