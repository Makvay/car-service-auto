package ru.car.dto.claim;

import lombok.Data;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ClaimDto {
    private Long id;
    private String claimNumber;
    private Long clientId;
    private String clientFirstName;
    private String clientLastName;
    private String clientPhone;
    private Long vehicleId;
    private String vehicleVin;
    private String vehicleLicensePlate;
    private String vehicleBrand;
    private String vehicleModel;
    private Long masterId;
    private String masterFirstName;
    private String masterLastName;
    private String masterSpecialization;
    private ClaimStatus status;
    private String problemDescription;
    private String customerNotes;
    private String internalNotes;
    private Integer mileageAtEntry;
    private ClaimPriority priority;
    private Boolean isApproved;
    private Boolean isPaid;
    private LocalDateTime createdAt;
    private LocalDate scheduledDate;
    private LocalDateTime diagnosisDate;
    private LocalDateTime startDate;
    private LocalDateTime completionDate;
    private LocalDateTime updatedAt;;
}
