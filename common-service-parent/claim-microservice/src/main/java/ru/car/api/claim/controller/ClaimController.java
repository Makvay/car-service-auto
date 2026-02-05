package ru.car.api.claim.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.car.api.claim.service.ClaimService;
import ru.car.dto.claim.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/claims")
@Tag(name = "Claim Management", description = "API для управления заявками на обслуживание")
public class ClaimController {

    private final ClaimService claimService;

    @PostMapping
    @Operation(summary = "Создать новую заявку")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Заявка успешно создана"),
            @ApiResponse(responseCode = "400", description = "Неверные данные запроса"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<ClaimDto> createClaim(@Valid @RequestBody CreateClaimRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(claimService.createClaim(request));
    }

    @GetMapping
    @Operation(summary = "Получить все заявки с фильтрами")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешное получение списка заявок"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<List<ClaimDto>> getAllClaims(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long masterId,
            @RequestParam(required = false) Boolean isPaid,
            @RequestParam(required = false) Long clientId) {

        if (clientId != null) {
            return ResponseEntity.ok(claimService.getClientClaims(clientId));
        }
        return ResponseEntity.ok(claimService.getAllClaims(status, masterId, isPaid));
    }

    @GetMapping("/search")
    @Operation(summary = "Расширенный поиск заявок с фильтрами")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешный поиск"),
            @ApiResponse(responseCode = "400", description = "Неверные параметры фильтрации"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<List<ClaimDto>> searchClaims(
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) Long vehicleId,
            @RequestParam(required = false) Long masterId,
            @RequestParam(required = false) List<ClaimStatus> statuses,
            @RequestParam(required = false) ClaimPriority priority)
    {
        ClaimSearchRequest searchRequest = new ClaimSearchRequest();
        searchRequest.setClientId(clientId);
        searchRequest.setVehicleId(vehicleId);
        searchRequest.setMasterId(masterId);
        searchRequest.setStatuses(statuses);
        searchRequest.setPriority(priority);

        List<ClaimDto> claims = claimService.searchClaims(searchRequest);
        return ResponseEntity.ok(claims);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить заявку по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Заявка найдена"),
            @ApiResponse(responseCode = "404", description = "Заявка не найдена"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера"),

    })
    public ResponseEntity<ClaimDto> getClaim(@PathVariable Long id) {
        return ResponseEntity.ok(claimService.getClaim(id));
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "Обновить статус заявки")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Статус успешно обновлен"),
            @ApiResponse(responseCode = "404", description = "Заявка не найдена"),
            @ApiResponse(responseCode = "400", description = "Неверный статус"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<ClaimDto> updateClaimStatus(
            @PathVariable Long id,
            @Valid @RequestBody ClaimStatusUpdateRequest request) {
        return ResponseEntity.ok(claimService.updateClaimStatus(id, request));
    }

    @PutMapping("/{id}/master")
    @Operation(summary = "Назначить мастера на заявку")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Мастер успешно назначен"),
            @ApiResponse(responseCode = "404", description = "Заявка не найдена"),
            @ApiResponse(responseCode = "400", description = "Неверный ID мастера"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<ClaimDto> assignMaster(
            @PathVariable Long id,
            @RequestParam Long masterId) {
        return ResponseEntity.ok(claimService.assignMaster(id, masterId));
    }

    @PostMapping("/{id}/work-items")
    @Operation(summary = "Добавить работу в заявку")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Работа успешно добавлена"),
            @ApiResponse(responseCode = "404", description = "Заявка не найдена"),
            @ApiResponse(responseCode = "400", description = "Неверные данные работы"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<ClaimDto> addWorkItem(
            @PathVariable Long id,
            @Valid @RequestBody WorkItemCreateRequest request) {
        return ResponseEntity.ok(claimService.addWorkItem(id, request));
    }


    @PostMapping("/{id}/parts")
    @Operation(summary = "Добавить запчасть в заявку")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запчасть успешно добавлена"),
            @ApiResponse(responseCode = "404", description = "Заявка не найдена"),
            @ApiResponse(responseCode = "400", description = "Неверные данные запчасти"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<ClaimDto> addPart(
            @PathVariable Long id,
            @Valid @RequestBody PartRequest request) {
        return ResponseEntity.ok(claimService.addPart(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить заявку")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Заявка успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Заявка не найдена"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<Void> deleteClaim(@PathVariable Long id) {
        claimService.deleteClaim(id);
        return ResponseEntity.noContent().build();
    }






}
