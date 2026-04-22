package ru.car.api.claim.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.car.api.claim.service.ClaimService;
import ru.car.dto.claim.ClaimDto;
import ru.car.dto.claim.ClaimPriority;
import ru.car.dto.claim.ClaimSearchRequest;
import ru.car.dto.claim.ClaimStatus;
import ru.car.dto.claim.ClaimStatusHistoryDto;
import ru.car.dto.claim.ClaimStatusUpdateRequest;
import ru.car.dto.claim.CreateClaimRequest;
import ru.car.dto.claim.PartRequest;
import ru.car.dto.claim.WorkItemCreateRequest;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping({"/api/claims", "/api/v1/claims"})
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
    @Operation(summary = "Получить все заявки с фильтрами (пагинация)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешное получение списка заявок"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<Page<ClaimDto>> getAllClaims(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long masterId,
            @RequestParam(required = false) Boolean isPaid,
            @RequestParam(required = false) Long clientId,
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable) {

        Page<ClaimDto> page = (clientId != null) ? claimService.getClientClaims(clientId, pageable)
                : claimService.getAllClaims(status, masterId, isPaid, pageable);
        // Ensure claimNumber is populated for all returned items
        Page<ClaimDto> enriched = page.map(dto -> {
            if (dto.getClaimNumber() == null) {
                dto.setClaimNumber("CL-" + dto.getId());
            }
            return dto;
        });
        return ResponseEntity.ok(enriched);
    }

    @GetMapping("/search")
    @Operation(summary = "Расширенный поиск заявок с фильтрами (пагинация)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Успешный поиск"),
            @ApiResponse(responseCode = "400", description = "Неверные параметры фильтрации"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<Page<ClaimDto>> searchClaims(
            @RequestParam(required = false) Long clientId,
            @RequestParam(required = false) Long vehicleId,
            @RequestParam(required = false) Long masterId,
            @RequestParam(required = false) List<ClaimStatus> statuses,
            @RequestParam(required = false) ClaimPriority priority,
            @PageableDefault(size = 20, sort = "createdAt") Pageable pageable)
    {
        ClaimSearchRequest searchRequest = new ClaimSearchRequest();
        searchRequest.setClientId(clientId);
        searchRequest.setVehicleId(vehicleId);
        searchRequest.setMasterId(masterId);
        searchRequest.setStatuses(statuses);
        searchRequest.setPriority(priority);

        Page<ClaimDto> claims = claimService.searchClaims(searchRequest, pageable);
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

    @GetMapping("/{id}/status-history")
    @Operation(summary = "Получить историю изменения статусов заявки")
    public ResponseEntity<List<ClaimStatusHistoryDto>> getStatusHistory(@PathVariable Long id) {
        return ResponseEntity.ok(claimService.getStatusHistory(id));
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

    @DeleteMapping("/{id}/parts/{claimPartId}")
    @Operation(summary = "Удалить запчасть из заявки")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Запчасть успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Запчасть не найдена"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<ClaimDto> deletePart(
            @PathVariable Long id,
            @PathVariable Long claimPartId) {
        return ResponseEntity.ok(claimService.deletePart(id, claimPartId));
    }

    @DeleteMapping("/{id}/work-items/{workItemId}")
    @Operation(summary = "Удалить работу из заявки")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Работа успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Работа не найдена"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public ResponseEntity<ClaimDto> deleteWorkItem(
            @PathVariable Long id,
            @PathVariable Long workItemId) {
        return ResponseEntity.ok(claimService.deleteWorkItem(id, workItemId));
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
