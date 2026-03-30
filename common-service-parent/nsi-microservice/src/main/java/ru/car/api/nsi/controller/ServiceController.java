package ru.car.api.nsi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Услуга найдена"),
            @ApiResponse(responseCode = "404", description = "Услуга не найдена"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @Operation(summary = "Получить услугу по ID")
    @GetMapping("/{id}")
    public ResponseEntity<ServiceDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceService.getById(id));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список услуг"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @Operation(summary = "Получить услуги по категории")
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ServiceDto>> getByCategoryId(@PathVariable Long categoryId) {
        return ResponseEntity.ok(serviceService.getByCategoryId(categoryId));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список услуг"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @Operation(summary = "Получить все услуги")
    @GetMapping
    public ResponseEntity<List<ServiceDto>> getAll() {
        return ResponseEntity.ok(serviceService.getAll());
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список активных услуг"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @Operation(summary = "Получить все активные услуги")
    @GetMapping("/active")
    public ResponseEntity<List<ServiceDto>> getAllActive() {
        return ResponseEntity.ok(serviceService.getAllActive());
    }
}
