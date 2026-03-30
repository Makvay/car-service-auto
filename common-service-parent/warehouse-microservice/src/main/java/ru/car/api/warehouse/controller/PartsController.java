package ru.car.api.warehouse.controller;


//POST /api/v1/parts - создать
//GET /api/v1/parts - все
//GET /api/v1/parts/{id} - по ID
//PUT /api/v1/parts/{id} - обновить
//DELETE /api/v1/parts/{id} - удалить

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.car.api.warehouse.service.PartService;
import ru.car.dto.warehouse.CreatePartRequest;
import ru.car.dto.warehouse.PartDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/parts")
public class PartsController {

    private final PartService partService;


    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Товар создан"),
            @ApiResponse(responseCode = "400", description = "Неверные данные"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @PostMapping
    @Operation(summary = "Создать товар")
    public ResponseEntity<PartDto> create (@RequestBody CreatePartRequest request) {
        return ResponseEntity.ok(partService.create(request));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешный поиск"),
            @ApiResponse(responseCode = "400", description = "Ошибка вводных данных"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping("/{id}")
    @Operation(summary = "Найти товар")
    public ResponseEntity<PartDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(partService.getById(id));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешный поиск"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @GetMapping
    @Operation(summary = "Показать все товары")
    public List<PartDto> findAll() {
        return partService.getAll();
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Данные обновлены"),
            @ApiResponse(responseCode = "404", description = "Товар не найден"),
            @ApiResponse(responseCode = "400", description = "Неверный ID товара"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @PutMapping("/{id}")
    @Operation(summary = "Обновить товар")
    public ResponseEntity<PartDto> update(
            @PathVariable Long id,
            @Valid @RequestBody CreatePartRequest request) {
        return ResponseEntity.ok(partService.update(id, request));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Товар удалён"),
            @ApiResponse(responseCode = "404", description = "Товар не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить товар")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        partService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
