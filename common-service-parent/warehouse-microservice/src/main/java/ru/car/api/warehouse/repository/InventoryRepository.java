package ru.car.api.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.car.entity.warehouse.InventoryEntity;

import java.util.List;

public interface InventoryRepository extends JpaRepository<InventoryEntity,Long> {

    List<InventoryEntity> findByPartId(Long partId);




}
