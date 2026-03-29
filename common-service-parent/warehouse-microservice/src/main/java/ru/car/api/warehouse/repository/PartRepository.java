package ru.car.api.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.car.entity.nsi.PartCategoryEntity;
import ru.car.entity.warehouse.PartEntity;
import ru.car.entity.warehouse.SupplyEntity;

import java.util.List;

public interface PartRepository extends JpaRepository<PartEntity,  Long> {

    List<SupplyEntity> findBySku(String sku);
    List<PartEntity> findByCategoryId(Long categoryId);
    List<PartEntity> findByIsActive(Boolean isActive);

}
