package ru.car.api.claim.controller;

import io.swagger.v3.oas.annotations.Operation;
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
    public ResponseEntity<ClaimDto> createClaim(@Valid @RequestBody CreateClaimRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(claimService.createClaim(request));
    }

    @GetMapping
    @Operation(summary = "Получить все заявки с фильтрами")
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

    @PutMapping("/{id}/status")
    @Operation(summary = "Обновить статус заявки")
    public ResponseEntity<ClaimDto> updateClaimStatus(
            @PathVariable Long id,
            @Valid @RequestBody ClaimStatusUpdateRequest request) {
        return ResponseEntity.ok(claimService.updateClaimStatus(id, request));
    }

    @PutMapping("/{id}/master")
    @Operation(summary = "Назначить мастера на заявку")
    public ResponseEntity<ClaimDto> assignMaster(
            @PathVariable Long id,
            @RequestParam Long masterId) {
        return ResponseEntity.ok(claimService.assignMaster(id, masterId));
    }

    @PostMapping("/{id}/work-items")
    @Operation(summary = "Добавить работу в заявку")
    public ResponseEntity<ClaimDto> addWorkItem(
            @PathVariable Long id,
            @Valid @RequestBody WorkItemCreateRequest request) {
        return ResponseEntity.ok(claimService.addWorkItem(id, request));
    }

    @PostMapping("/{id}/parts")
    @Operation(summary = "Добавить запчасть в заявку")
    public ResponseEntity<ClaimDto> addPart(
            @PathVariable Long id,
            @Valid @RequestBody PartRequest request) {
        return ResponseEntity.ok(claimService.addPart(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить заявку")
    public ResponseEntity<Void> deleteClaim(@PathVariable Long id) {
        claimService.deleteClaim(id);
        return ResponseEntity.noContent().build();
    }






}
