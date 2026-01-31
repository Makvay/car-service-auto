package ru.car.dto.master;

import lombok.Data;

import ru.car.dto.nsi.ServiceDto;

import java.time.LocalDateTime;

@Data
public class MasterWorkDto {
    private Long id;

    private MasterDto master;
    private Long claimId;
    private ServiceDto service;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double hoursWorked;
    private String notes;
    private LocalDateTime createdAt;
}