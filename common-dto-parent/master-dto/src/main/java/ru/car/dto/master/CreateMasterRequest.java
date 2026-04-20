package ru.car.dto.master;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;

@Data
public class CreateMasterRequest {
    @NotBlank(message = "Табельный номер обязателен")
    private String employeeCode;

    @NotBlank(message = "Имя обязательно")
    private String firstName;

    @NotBlank(message = "Фамилия обязательна")
    private String lastName;

    @NotBlank(message = "Телефон обязателен")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Неверный формат телефона")
    private String phone;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Неверный формат email")
    private String email;

    private List<String> specializations;

    private String qualificationLevel;

    @NotNull(message = "Ставка в час обязательна")
    private Double hourlyRate;

    @NotNull(message = "Дата приема обязательна")
    private LocalDate hireDate;
}