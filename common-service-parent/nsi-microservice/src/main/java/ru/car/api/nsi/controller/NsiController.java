package ru.car.api.nsi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping("/health")
    @Operation(summary = "Проверка здоровья сервиса")
    public Map<String, String> health() {
        return Map.of("status", "UP", "service", "NSI Service");
    }

    // ✅ Марки авто - GET /api/nsi/vehicle-brands
    @GetMapping("/vehicle-brands")
    @Operation(summary = "Получить все марки автомобилей")
    public List<CarStampDto> getAllBrands() {
        return carStampService.getAllActive();
    }

    @GetMapping("/vehicle-brands/{id}")
    @Operation(summary = "Получить марку авто по ID")
    public CarStampDto getBrandById(@PathVariable Long id) {
        return carStampService.getById(id);
    }

    // ✅ Услуги - GET /api/nsi/services
    @GetMapping("/services")
    @Operation(summary = "Получить все виды услуг")
    public List<ServiceDto> getAllServices() {
        return serviceService.getAllActive();
    }

    @GetMapping("/services/{id}")
    @Operation(summary = "Получить услугу по ID")
    public ServiceDto getServiceById(@PathVariable Long id) {
        return serviceService.getById(id);
    }

    @GetMapping("/services/category/{categoryId}")
    @Operation(summary = "Получить услуги по категории")
    public List<ServiceDto> getServicesByCategory(@PathVariable Long categoryId) {
        return serviceService.getByCategoryId(categoryId);
    }

    // ✅ Категории запчастей - GET /api/nsi/part-categories
    @GetMapping("/part-categories")
    @Operation(summary = "Получить все категории запчастей")
    public List<PartCategoryDto> getAllPartCategories() {
        return partCategoryService.getAllActive();
    }

    @GetMapping("/part-categories/{id}")
    @Operation(summary = "Получить категорию запчастей по ID")
    public PartCategoryDto getPartCategoryById(@PathVariable Long id) {
        return partCategoryService.getById(id);
    }

    @GetMapping("/part-categories/root")
    @Operation(summary = "Получить корневые категории запчастей")
    public List<PartCategoryDto> getRootCategories() {
        return partCategoryService.getRootCategories();
    }

    @GetMapping("/part-categories/{parentId}/children")
    @Operation(summary = "Получить дочерние категории")
    public List<PartCategoryDto> getChildCategories(@PathVariable Long parentId) {
        return partCategoryService.getByParentId(parentId);
    }
}