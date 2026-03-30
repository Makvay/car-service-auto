package ru.car.api.notification.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerCfg {

    @Bean
    public OpenAPI masterServiceOpenAPI() {
        return new OpenAPI().info(new Info().title("Апи уведомлений").description("Апи").version("1.0"));
    }
}