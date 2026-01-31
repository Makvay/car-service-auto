package ru.car.dto.claim;

import lombok.Data;
import ru.car.dto.client.ClientCarDto;
import ru.car.dto.client.ClientDto;
import ru.car.dto.master.MasterDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ClaimDto {
    private Long id;
    private String claimNumber;


    private ClientDto client;
    private ClientCarDto vehicle;
    private MasterDto master;

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
    private LocalDateTime updatedAt;
}