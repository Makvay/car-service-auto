package ru.car.api.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.car.entity.warehouse.SupplyEntity;

public interface SupplyItemRepository extends JpaRepository<SupplyEntity, Long> {

}
