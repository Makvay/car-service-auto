package ru.car.dto.client;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientCreatedEvent {
    private Long clientId;
    private String phone;
}
