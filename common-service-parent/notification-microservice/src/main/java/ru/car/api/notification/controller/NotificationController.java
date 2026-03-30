package ru.car.api.notification.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.car.api.notification.service.NotificationService;
import ru.car.dto.notification.NotificationDto;


import java.util.List;

//GET /api/notifications - все уведомления
//GET /api/notifications/client/{clientId} - уведомления клиента

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notification")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public List<NotificationDto> findAll() {
        return notificationService.getAll();
    }

    @GetMapping("/client/{clientId}")
    public List<NotificationDto> findById(@PathVariable Long clientId) {
        return notificationService.getByClientId(clientId);

    }


}
