package ru.car.dto.client;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MileageUpdatedEvent {
    private Long vehicleId;
    private Integer mileage;
}
