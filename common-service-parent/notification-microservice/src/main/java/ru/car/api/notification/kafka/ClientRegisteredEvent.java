package ru.car.api.notification.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientRegisteredEvent {
    private Long clientId;
    private String email;
    private String firstName;
    private String lastName;
}