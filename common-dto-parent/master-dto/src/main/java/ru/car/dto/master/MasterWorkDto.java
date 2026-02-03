package ru.car.dto.master;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MasterWorkDto {
    private Long id;


    private Long masterId;
    private String masterFirstName;
    private String masterLastName;
    private String masterSpecialization;

    private Long claimId;
    private String claimNumber;


    private Long serviceId;
    private String serviceName;
    private String serviceCode;
    private String serviceDescription;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double hoursWorked;
    private String notes;
    private LocalDateTime createdAt;
}