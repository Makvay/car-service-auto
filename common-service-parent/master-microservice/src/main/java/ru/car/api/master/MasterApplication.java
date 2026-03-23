package ru.car.api.master;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"ru.car.entity.master", "ru.car.entity"})
@EnableJpaRepositories(basePackages = "ru.car.api.master.repository")

public class MasterApplication {
    public static void main(String[] args) {
        SpringApplication.run(MasterApplication.class, args);
    }
}