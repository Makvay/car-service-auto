package ru.car.api.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.car.entity.warehouse.SupplyItemEntity;

public interface SupplyItemRepository extends JpaRepository<SupplyItemEntity, Long> {

}
