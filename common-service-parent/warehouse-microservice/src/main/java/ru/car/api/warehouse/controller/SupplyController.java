package ru.car.api.warehouse.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.car.api.warehouse.service.SupplyService;
import ru.car.dto.warehouse.SupplyDto;

import java.util.List;

//GET /api/v1/supplies - все поставки
//GET /api/v1/supplies/{id} - по ID
//PUT /api/v1/supplies/{id}/status - обновить статус

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/supplies")
@Tag(name = "Supplies", description = "Поставки")
public class SupplyController {

    private final SupplyService supplyService;

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список поставок получен"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @GetMapping
    @Operation(summary = "Получить все поставки")
    public ResponseEntity<List<SupplyDto>> findAll() {
        return ResponseEntity.ok(supplyService.getAll());
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Поставка найдена"),
            @ApiResponse(responseCode = "404", description = "Поставка не найдена"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @GetMapping("/{id}")
    @Operation(summary = "Найти поставку по ID")
    public ResponseEntity<SupplyDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(supplyService.getById(id));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Статус обновлён"),
            @ApiResponse(responseCode = "404", description = "Поставка не найдена"),
            @ApiResponse(responseCode = "400", description = "Неверный статус"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @PutMapping("/{id}/status")
    @Operation(summary = "Обновить статус поставки")
    public ResponseEntity<SupplyDto> updateStatus(
            @PathVariable Long id, 
            @RequestParam String status) {
        return ResponseEntity.ok(supplyService.updateStatus(id, status));
    }
}
