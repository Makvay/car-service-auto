package ru.car.api.nsi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("ru.car.entity.nsi")  // Сканирует entity
@EnableJpaRepositories("ru.car.api.nsi.repository")  // Сканирует репозитории
public class NsiApplication {
    public static void main(String[] args) {
        SpringApplication.run(NsiApplication.class, args);
    }
}