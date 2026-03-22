package ru.car.api.nsi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.car.entity.nsi.ServiceEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<ServiceEntity, Long> {

    Optional<ServiceEntity> findByCode(String code);

    List<ServiceEntity> findByIsActiveTrue();


    List<ServiceEntity> findByCategoryId(Long categoryId);


    List<ServiceEntity> findByCategoryIdAndIsActiveTrue(Long categoryId);

    boolean existsByCode(String code);
}