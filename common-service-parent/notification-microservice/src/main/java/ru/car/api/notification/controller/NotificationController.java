package ru.car.api.notification.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.car.api.notification.service.NotificationService;
import ru.car.dto.notification.NotificationDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
@Tag(name = "Notification", description = "Уведомления")
public class NotificationController {

    private final NotificationService notificationService;

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список уведомлений"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @Operation(summary = "Получить все уведомления")
    @GetMapping
    public ResponseEntity<List<NotificationDto>> findAll() {
        return ResponseEntity.ok(notificationService.getAll());
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список уведомлений клиента"),
            @ApiResponse(responseCode = "404", description = "Клиент не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка")
    })
    @Operation(summary = "Получить уведомления клиента")
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<NotificationDto>> findByClientId(@PathVariable Long clientId) {
        return ResponseEntity.ok(notificationService.getByClientId(clientId));
    }
}
