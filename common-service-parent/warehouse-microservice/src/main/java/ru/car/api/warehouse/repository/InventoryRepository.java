package ru.car.api.warehouse.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.car.entity.warehouse.InventoryEntity;

import java.util.List;

public interface InventoryRepository extends JpaRepository<InventoryEntity,Long> {

    List<InventoryEntity> findByPartId(Long partId);
    List<InventoryEntity> findByPartIdOrderByIdAsc(Long partId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<InventoryEntity> findWithLockByPartIdOrderByIdAsc(Long partId);

}
