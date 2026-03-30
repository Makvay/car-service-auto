package ru.car.api.warehouse.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.car.api.warehouse.mapper.InventoryMapper;
import ru.car.api.warehouse.service.InventoryService;
import ru.car.dto.warehouse.InventoryDto;

import java.util.List;

//GET /api/v1/inventory/part/{partId} - остаток по запчасти
//PUT /api/v1/inventory/{id}/quantity - обновить количество

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/part")
public class InventoryController {

    private final InventoryService inventoryService;
    private final InventoryMapper inventoryMapper;


    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @GetMapping
    @Operation(summary = "Получить остаток")
    public List<InventoryDto> getByPartId(@PathVariable Long partId) {
        return inventoryService.getByPartId(partId);
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешно"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @PutMapping
    @Operation(summary = "Обновить кол-во товар(ов)")
    public ResponseEntity<InventoryDto> update(
            @PathVariable Long id,
             @RequestParam Integer quantity) {
        return ResponseEntity.ok(inventoryService.updateQuantity(id, quantity));
    }





}
