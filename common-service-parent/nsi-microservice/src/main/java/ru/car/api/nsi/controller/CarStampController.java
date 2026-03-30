package ru.car.api.nsi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.car.api.nsi.service.CarStampService;
import ru.car.dto.nsi.CarStampDto;

import java.util.List;

@RestController
@RequestMapping("/internal/car-stamps")
@RequiredArgsConstructor
@Tag(name = "Car Stamp Internal", description = "Внутреннее API для марок автомобилей")
public class CarStampController {

    private final CarStampService carStampService;

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Марка найдена"),
            @ApiResponse(responseCode = "404", description = "Марка не найдена"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @Operation(summary = "Получить марку автомобиля по ID")
    @GetMapping("/{id}")
    public ResponseEntity<CarStampDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(carStampService.getById(id));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Марка найдена"),
            @ApiResponse(responseCode = "404", description = "Марка не найдена"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @Operation(summary = "Получить марку автомобиля по коду")
    @GetMapping("/code/{code}")
    public ResponseEntity<CarStampDto> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(carStampService.getByCode(code));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список марок"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @Operation(summary = "Получить все марки автомобилей")
    @GetMapping
    public ResponseEntity<List<CarStampDto>> getAll() {
        return ResponseEntity.ok(carStampService.getAll());
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список активных марок"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @Operation(summary = "Получить все активные марки автомобилей")
    @GetMapping("/active")
    public ResponseEntity<List<CarStampDto>> getAllActive() {
        return ResponseEntity.ok(carStampService.getAllActive());
    }
}
