package ru.car.api.warehouse.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.car.api.warehouse.service.ReservationService;
import ru.car.dto.warehouse.ReservationDto;

import java.util.List;

//POST /api/v1/reservations - создать резерв
//GET /api/v1/reservations/claim/{claimId} - резервы по заявке

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservations")
@Tag(name = "Reservations", description = "Резервации запчастей")
public class ReservationController {

    private final ReservationService reservationService;

    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Бронь создана"),
            @ApiResponse(responseCode = "400", description = "Неверные данные"),
            @ApiResponse(responseCode = "404", description = "Запчасть не найдена"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @PostMapping
    @Operation(summary = "Создать бронь")
    public ResponseEntity<ReservationDto> createReserve(@RequestBody ReservationDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationService.createReservation(dto));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Резервации найдены"),
            @ApiResponse(responseCode = "404", description = "Заявка не найдена"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @GetMapping("/claim/{claimId}")
    @Operation(summary = "Найти брони по заявке")
    public ResponseEntity<List<ReservationDto>> getByClaimId(@PathVariable Long claimId) {
        return ResponseEntity.ok(reservationService.getByClaimId(claimId));
    }
}
