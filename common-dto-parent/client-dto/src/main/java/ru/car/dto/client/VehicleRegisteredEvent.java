package ru.car.dto.client;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VehicleRegisteredEvent {
    private Long vehicleId;
    private Long clientId;
    private String vin;

}
