package ru.car.dto.claim;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;


@Data
public class CreateClaimRequest {
    @NotNull @Positive
    private Long clientId;

    @NotNull @Positive
    private Long vehicleId;

    @NotNull
    private String problemDescription;

    private String customerNotes;
    private String internalNotes;

    @NotNull @Positive
    private Integer mileageAtEntry;

    private String priority;
    private LocalDate scheduledDate;

}