package ru.car.api.nsi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.car.entity.nsi.PartCategoryEntity; // <-- Измените импорт

import java.util.List;
import java.util.Optional;

@Repository
public interface PartCategoryRepository extends JpaRepository<PartCategoryEntity, Long> {

    Optional<PartCategoryEntity> findByCode(String code);

    List<PartCategoryEntity> findByIsActiveTrue();

    List<PartCategoryEntity> findByParentIdIsNull();

    List<PartCategoryEntity> findByParentId(Long parentId);

    List<PartCategoryEntity> findByParentIdAndIsActiveTrue(Long parentId);
}