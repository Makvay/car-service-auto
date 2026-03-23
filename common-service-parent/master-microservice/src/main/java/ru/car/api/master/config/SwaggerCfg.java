package ru.car.api.master.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerCfg {

    @Bean
    public OpenAPI masterServiceOpenAPI() {
        return new OpenAPI().info(new Info().title("Апи заявки").description("Апи").version("1.0"));
    }
}
