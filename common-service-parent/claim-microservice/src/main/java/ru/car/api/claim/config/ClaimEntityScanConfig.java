package ru.car.api.claim.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration

@EntityScan(basePackages = {"ru.car.entity.claim", "ru.car.entity"})
public class ClaimEntityScanConfig {
}