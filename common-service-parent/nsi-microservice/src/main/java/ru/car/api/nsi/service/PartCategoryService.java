package ru.car.api.nsi.service;

import ru.car.dto.nsi.CreatePartCategoryRequest;
import ru.car.dto.nsi.PartCategoryDto;
import ru.car.dto.nsi.UpdatePartCategoryRequest;

import java.util.List;

public interface PartCategoryService {
    PartCategoryDto getById(Long id);
    PartCategoryDto getByCode(String code);
    List<PartCategoryDto> getAll();
    List<PartCategoryDto> getAllActive();
    List<PartCategoryDto> getRootCategories();
    List<PartCategoryDto> getByParentId(Long parentId);
}
