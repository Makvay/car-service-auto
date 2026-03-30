package ru.car.api.nsi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.car.api.nsi.service.CarModelService;
import ru.car.dto.nsi.CarModelDto;

import java.util.List;

@RestController
@RequestMapping("/internal/car-models")
@RequiredArgsConstructor
@Tag(name = "Car Model Internal", description = "Внутреннее API для моделей автомобилей")
public class CarModelController {

    private final CarModelService carModelService;

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Модель найдена"),
            @ApiResponse(responseCode = "404", description = "Модель не найдена"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @Operation(summary = "Получить модель автомобиля по ID")
    @GetMapping("/{id}")
    public ResponseEntity<CarModelDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(carModelService.getById(id));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список моделей"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @Operation(summary = "Получить модели по марке автомобиля")
    @GetMapping("/stamp/{stampId}")
    public ResponseEntity<List<CarModelDto>> getByStampId(@PathVariable Long stampId) {
        return ResponseEntity.ok(carModelService.getByStampId(stampId));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список моделей"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @Operation(summary = "Получить все модели автомобилей")
    @GetMapping
    public ResponseEntity<List<CarModelDto>> getAll() {
        return ResponseEntity.ok(carModelService.getAll());
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список активных моделей"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @Operation(summary = "Получить все активные модели автомобилей")
    @GetMapping("/active")
    public ResponseEntity<List<CarModelDto>> getAllActive() {
        return ResponseEntity.ok(carModelService.getAllActive());
    }
}
