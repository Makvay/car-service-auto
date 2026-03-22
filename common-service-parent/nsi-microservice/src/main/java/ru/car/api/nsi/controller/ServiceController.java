package ru.car.api.nsi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.car.api.nsi.service.ServiceService;
import ru.car.dto.nsi.ServiceDto;

import java.util.List;

@RestController
@RequestMapping("/internal/services")
@RequiredArgsConstructor
@Tag(name = "Service Internal", description = "Внутреннее API для услуг")
public class ServiceController {

    private final ServiceService serviceService;

    @GetMapping("/{id}")
    @Operation(summary = "Получить услугу по ID")
    public ServiceDto getById(@PathVariable Long id) {
        return serviceService.getById(id);
    }

    @GetMapping("/category/{categoryId}")
    @Operation(summary = "Получить услуги по категории")
    public List<ServiceDto> getByCategoryId(@PathVariable Long categoryId) {
        return serviceService.getByCategoryId(categoryId);
    }

    @GetMapping
    @Operation(summary = "Получить все услуги")
    public List<ServiceDto> getAll() {
        return serviceService.getAll();
    }

    @GetMapping("/active")
    @Operation(summary = "Получить все активные услуги")
    public List<ServiceDto> getAllActive() {
        return serviceService.getAllActive();
    }
}