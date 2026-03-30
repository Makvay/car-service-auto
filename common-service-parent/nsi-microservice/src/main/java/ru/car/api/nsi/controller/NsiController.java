package ru.car.api.nsi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.car.api.nsi.service.CarStampService;
import ru.car.api.nsi.service.PartCategoryService;
import ru.car.api.nsi.service.ServiceService;
import ru.car.dto.nsi.CarStampDto;
import ru.car.dto.nsi.PartCategoryDto;
import ru.car.dto.nsi.ServiceDto;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/nsi")
@RequiredArgsConstructor
@Tag(name = "NSI Service", description = "Справочники системы")
public class NsiController {

    private final CarStampService carStampService;
    private final ServiceService serviceService;
    private final PartCategoryService partCategoryService;

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Сервис работает"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @Operation(summary = "Проверка здоровья сервиса")
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "NSI Service"));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список марок автомобилей"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @Operation(summary = "Получить все марки автомобилей")
    @GetMapping("/vehicle-brands")
    public ResponseEntity<List<CarStampDto>> getAllBrands() {
        return ResponseEntity.ok(carStampService.getAllActive());
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Марка найдена"),
            @ApiResponse(responseCode = "404", description = "Марка не найдена"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @Operation(summary = "Получить марку авто по ID")
    @GetMapping("/vehicle-brands/{id}")
    public ResponseEntity<CarStampDto> getBrandById(@PathVariable Long id) {
        return ResponseEntity.ok(carStampService.getById(id));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список услуг"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @Operation(summary = "Получить все виды услуг")
    @GetMapping("/services")
    public ResponseEntity<List<ServiceDto>> getAllServices() {
        return ResponseEntity.ok(serviceService.getAllActive());
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Услуга найдена"),
            @ApiResponse(responseCode = "404", description = "Услуга не найдена"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @Operation(summary = "Получить услугу по ID")
    @GetMapping("/services/{id}")
    public ResponseEntity<ServiceDto> getServiceById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceService.getById(id));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список услуг"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @Operation(summary = "Получить услуги по категории")
    @GetMapping("/services/category/{categoryId}")
    public ResponseEntity<List<ServiceDto>> getServicesByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(serviceService.getByCategoryId(categoryId));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список категорий запчастей"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @Operation(summary = "Получить все категории запчастей")
    @GetMapping("/part-categories")
    public ResponseEntity<List<PartCategoryDto>> getAllPartCategories() {
        return ResponseEntity.ok(partCategoryService.getAllActive());
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Категория найдена"),
            @ApiResponse(responseCode = "404", description = "Категория не найдена"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @Operation(summary = "Получить категорию запчастей по ID")
    @GetMapping("/part-categories/{id}")
    public ResponseEntity<PartCategoryDto> getPartCategoryById(@PathVariable Long id) {
        return ResponseEntity.ok(partCategoryService.getById(id));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список корневых категорий"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @Operation(summary = "Получить корневые категории запчастей")
    @GetMapping("/part-categories/root")
    public ResponseEntity<List<PartCategoryDto>> getRootCategories() {
        return ResponseEntity.ok(partCategoryService.getRootCategories());
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список дочерних категорий"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @Operation(summary = "Получить дочерние категории")
    @GetMapping("/part-categories/{parentId}/children")
    public ResponseEntity<List<PartCategoryDto>> getChildCategories(@PathVariable Long parentId) {
        return ResponseEntity.ok(partCategoryService.getByParentId(parentId));
    }
}
