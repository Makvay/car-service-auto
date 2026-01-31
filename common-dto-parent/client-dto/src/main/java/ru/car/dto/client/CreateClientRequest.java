package ru.car.dto.client;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Data
public class CreateClientRequest {
    @NotBlank(message = "Имя обязательно")
    private String firstName;

    @NotBlank(message = "Фамилия обязательна")
    private String lastName;

    @NotBlank(message = "Телефон обязателен")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Неверный формат телефона")
    private String phone;

    private String email;
}