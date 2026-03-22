package ru.car.api.warehouse.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerCfg {
    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI().info(new Info().title("Апи Клиенты и автомобили").description("Апи").version("1.0"));
    }
}
