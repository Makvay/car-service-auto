package ru.car.api.claim.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.car.dto.notification.EmailRequestDto;

@FeignClient(name = "notification-service", url = "${services.notification.url:http://localhost:8085}", fallback = NotificationFeignClientFallback.class)
public interface NotificationFeignClient {

    @PostMapping("/api/v1/notification/email")
    void sendEmail(@RequestBody EmailRequestDto request);
}
