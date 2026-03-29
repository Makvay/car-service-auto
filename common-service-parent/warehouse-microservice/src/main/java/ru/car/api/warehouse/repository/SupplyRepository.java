package ru.car.api.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.car.entity.warehouse.SupplyEntity;

import java.util.List;

public interface SupplyRepository extends JpaRepository<SupplyEntity, Long> {

    List<SupplyEntity> findByStatus(String status);

}
