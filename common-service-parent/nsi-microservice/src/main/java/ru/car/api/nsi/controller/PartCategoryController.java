package ru.car.api.nsi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Категория найдена"),
            @ApiResponse(responseCode = "404", description = "Категория не найдена"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @Operation(summary = "Получить категорию запчастей по ID")
    @GetMapping("/{id}")
    public ResponseEntity<PartCategoryDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(partCategoryService.getById(id));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Категория найдена"),
            @ApiResponse(responseCode = "404", description = "Категория не найдена"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @Operation(summary = "Получить категорию запчастей по коду")
    @GetMapping("/code/{code}")
    public ResponseEntity<PartCategoryDto> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(partCategoryService.getByCode(code));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список корневых категорий"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @Operation(summary = "Получить корневые категории")
    @GetMapping("/root")
    public ResponseEntity<List<PartCategoryDto>> getRootCategories() {
        return ResponseEntity.ok(partCategoryService.getRootCategories());
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список дочерних категорий"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @Operation(summary = "Получить дочерние категории")
    @GetMapping("/{parentId}/children")
    public ResponseEntity<List<PartCategoryDto>> getChildren(@PathVariable Long parentId) {
        return ResponseEntity.ok(partCategoryService.getByParentId(parentId));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список категорий"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @Operation(summary = "Получить все категории")
    @GetMapping
    public ResponseEntity<List<PartCategoryDto>> getAll() {
        return ResponseEntity.ok(partCategoryService.getAll());
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список активных категорий"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @Operation(summary = "Получить все активные категории")
    @GetMapping("/active")
    public ResponseEntity<List<PartCategoryDto>> getAllActive() {
        return ResponseEntity.ok(partCategoryService.getAllActive());
    }
}
