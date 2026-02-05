package ru.car.api.nsi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerCfg {
    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI().info(new Info().title("Справочники").description("Апи").version("1.0"));
    }
}
