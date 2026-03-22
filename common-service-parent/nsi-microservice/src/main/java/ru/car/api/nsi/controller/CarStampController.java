package ru.car.api.nsi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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

  @GetMapping("/{id}")
  @Operation(summary = "Получить марку автомобиля по ID")
  public CarStampDto getById(@PathVariable Long id) {
    return carStampService.getById(id);
  }

  @GetMapping("/code/{code}")
  @Operation(summary = "Получить марку автомобиля по коду")
  public CarStampDto getByCode(@PathVariable String code) {
    return carStampService.getByCode(code);
  }

  @GetMapping
  @Operation(summary = "Получить все марки автомобилей")
  public List<CarStampDto> getAll() {
    return carStampService.getAll();
  }

  @GetMapping("/active")
  @Operation(summary = "Получить все активные марки автомобилей")
  public List<CarStampDto> getAllActive() {
    return carStampService.getAllActive();
  }
}