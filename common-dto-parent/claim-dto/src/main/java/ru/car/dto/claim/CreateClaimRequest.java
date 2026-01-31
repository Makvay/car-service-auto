package ru.car.dto.claim;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class CreateClaimRequest {
    @NotNull(message = "ID клиента обязательно")
    private Long clientId;

    @NotNull(message = "ID автомобиля обязательно")
    private Long vehicleId;

    @NotBlank(message = "Описание проблемы обязательно")
    private String problemDescription;

    private Long masterId;
    private String priority;
    private Integer mileageAtEntry;
}