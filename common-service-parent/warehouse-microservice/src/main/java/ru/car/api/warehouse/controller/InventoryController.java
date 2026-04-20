package ru.car.api.warehouse.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.car.api.warehouse.service.InventoryService;
import ru.car.dto.warehouse.DeductRequest;
import ru.car.dto.warehouse.InventoryDto;

import java.util.List;

//GET /api/v1/inventory/part/{partId} - остаток по запчасти
//PUT /api/v1/inventory/{id}/quantity - обновить количество

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/inventory")
@Tag(name = "Inventory", description = "Складской учёт")
public class InventoryController {

    private final InventoryService inventoryService;

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Остаток найден"),
            @ApiResponse(responseCode = "404", description = "Запчасть не найдена"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @GetMapping("/part/{partId}")
    @Operation(summary = "Получить остаток по запчасти")
    public ResponseEntity<List<InventoryDto>> getByPartId(@PathVariable Long partId) {
        return ResponseEntity.ok(inventoryService.getByPartId(partId));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Количество обновлено"),
            @ApiResponse(responseCode = "404", description = "Остаток не найден"),
            @ApiResponse(responseCode = "400", description = "Неверные данные"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @PutMapping("/{id}/quantity")
    @Operation(summary = "Обновить количество товара")
    public ResponseEntity<InventoryDto> updateQuantity(
            @PathVariable Long id,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(inventoryService.updateQuantity(id, quantity));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Товары списаны"),
            @ApiResponse(responseCode = "400", description = "Ошибка списания"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @PostMapping("/deduct")
    @Operation(summary = "Списать товары со склада")
    public ResponseEntity<Void> deductInventory(@RequestBody DeductRequest request) {
        inventoryService.deductInventory(request);
        return ResponseEntity.ok().build();
    }
}
