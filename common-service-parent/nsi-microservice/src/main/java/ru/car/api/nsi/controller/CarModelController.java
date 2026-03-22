package ru.car.api.nsi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/{id}")
    @Operation(summary = "Получить модель автомобиля по ID")
    public CarModelDto getById(@PathVariable Long id) {
        return carModelService.getById(id);
    }

    @GetMapping("/stamp/{stampId}")
    @Operation(summary = "Получить модели по марке автомобиля")
    public List<CarModelDto> getByStampId(@PathVariable Long stampId) {
        return carModelService.getByStampId(stampId);
    }

    @GetMapping
    @Operation(summary = "Получить все модели автомобилей")
    public List<CarModelDto> getAll() {
        return carModelService.getAll();
    }

    @GetMapping("/active")
    @Operation(summary = "Получить все активные модели автомобилей")
    public List<CarModelDto> getAllActive() {
        return carModelService.getAllActive();
    }
}