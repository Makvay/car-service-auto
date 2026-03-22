package ru.car.dto.nsi;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateCarModelRequest {

    @Size(max = 50)
    private String code;      // ← для getCode()

    @Size(max = 100)
    private String name;

    private Long stampId;     // ← для getStampId()

    @Size(max = 50)
    private String productionYears;

    private VehicleType vehicleType;

    private Boolean isActive;

}
