package ru.car.api.nsi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.car.api.nsi.service.PartCategoryService;
import ru.car.dto.nsi.PartCategoryDto;

import java.util.List;

@RestController
@RequestMapping("/internal/part-categories")
@RequiredArgsConstructor
@Tag(name = "Part Category Internal", description = "Внутреннее API для категорий запчастей")
public class PartCategoryController {

    private final PartCategoryService partCategoryService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить категорию запчастей по ID")
    public PartCategoryDto getById(@PathVariable Long id) {
        return partCategoryService.getById(id);
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Получить категорию запчастей по коду")
    public PartCategoryDto getByCode(@PathVariable String code) {
        return partCategoryService.getByCode(code);
    }

    @GetMapping("/root")
    @Operation(summary = "Получить корневые категории")
    public List<PartCategoryDto> getRootCategories() {
        return partCategoryService.getRootCategories();
    }

    @GetMapping("/{parentId}/children")
    @Operation(summary = "Получить дочерние категории")
    public List<PartCategoryDto> getChildren(@PathVariable Long parentId) {
        return partCategoryService.getByParentId(parentId);
    }

    @GetMapping
    @Operation(summary = "Получить все категории")
    public List<PartCategoryDto> getAll() {
        return partCategoryService.getAll();
    }

    @GetMapping("/active")
    @Operation(summary = "Получить все активные категории")
    public List<PartCategoryDto> getAllActive() {
        return partCategoryService.getAllActive();
    }
}