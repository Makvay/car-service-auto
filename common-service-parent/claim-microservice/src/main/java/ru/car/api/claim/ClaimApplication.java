package ru.car.api.claim;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EntityScan(basePackages = {
        "ru.car.entity.claim",
        "ru.car.entity"})

@EnableFeignClients
@EnableScheduling
@SpringBootApplication
public class ClaimApplication {
    public static void main(String[] args) {
        SpringApplication.run(ClaimApplication.class, args);
    }
}
