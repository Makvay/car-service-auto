package ru.car.dto.client;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ClientDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}