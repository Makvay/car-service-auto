package ru.car.dto.master;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class MasterDto {
    private Long id;
    private String employeeCode;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String specialization;
    private String qualificationLevel;
    private Double hourlyRate;
    private Boolean isActive;
    private LocalDate hireDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}