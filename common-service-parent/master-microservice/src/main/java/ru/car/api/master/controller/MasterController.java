package ru.car.api.master.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.car.api.master.service.MasterService;
import ru.car.dto.master.CreateMasterRequest;
import ru.car.dto.master.MasterDto;

import java.util.List;

@Tag(name = "Master Management", description = "API для управления мастерами")
@RestController
@RequestMapping("/api/v1/masters")
@RequiredArgsConstructor
public class MasterController {

    private final MasterService masterService;

    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Мастер создан"),
            @ApiResponse(responseCode = "400", description = "Неверные данные"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @Operation(summary = "Создать мастера")
    @PostMapping
    public ResponseEntity<MasterDto> createMaster(@Valid @RequestBody CreateMasterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(masterService.createMaster(request));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Мастер найден"),
            @ApiResponse(responseCode = "404", description = "Мастер не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @Operation(summary = "Получить мастера по ID")
    @GetMapping("/{id}")
    public ResponseEntity<MasterDto> getMasterById(@PathVariable Long id) {
        return ResponseEntity.ok(masterService.getMasterById(id));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Мастер найден"),
            @ApiResponse(responseCode = "404", description = "Мастер не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @Operation(summary = "Получить мастера по табельному номеру")
    @GetMapping("/code/{employeeCode}")
    public ResponseEntity<MasterDto> getMasterByEmployeeCode(@PathVariable String employeeCode) {
        return ResponseEntity.ok(masterService.getMasterByEmployeeCode(employeeCode));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список мастеров"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @Operation(summary = "Получить всех мастеров")
    @GetMapping
    public ResponseEntity<List<MasterDto>> getAllMasters() {
        return ResponseEntity.ok(masterService.getAllMasters());
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список активных мастеров"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @Operation(summary = "Получить активных мастеров")
    @GetMapping("/active")
    public ResponseEntity<List<MasterDto>> getActiveMasters() {
        return ResponseEntity.ok(masterService.getActiveMasters());
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список мастеров по специализации"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @Operation(summary = "Получить мастеров по специализации")
    @GetMapping("/specialization/{specialization}")
    public ResponseEntity<List<MasterDto>> getMastersBySpecialization(@PathVariable String specialization) {
        return ResponseEntity.ok(masterService.getMastersBySpecialization(specialization));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Мастер обновлён"),
            @ApiResponse(responseCode = "400", description = "Неверные данные"),
            @ApiResponse(responseCode = "404", description = "Мастер не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @Operation(summary = "Обновить мастера")
    @PutMapping("/{id}")
    public ResponseEntity<MasterDto> updateMaster(
            @PathVariable Long id,
            @Valid @RequestBody CreateMasterRequest request) {
        return ResponseEntity.ok(masterService.updateMaster(id, request));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Мастер деактивирован"),
            @ApiResponse(responseCode = "404", description = "Мастер не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @Operation(summary = "Деактивировать мастера")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateMaster(@PathVariable Long id) {
        masterService.deactivateMaster(id);
        return ResponseEntity.noContent().build();
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Мастер активирован"),
            @ApiResponse(responseCode = "404", description = "Мастер не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @Operation(summary = "Активировать мастера")
    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activateMaster(@PathVariable Long id) {
        masterService.activateMaster(id);
        return ResponseEntity.ok().build();
    }
}
