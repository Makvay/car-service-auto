package ru.car.dto.warehouse;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class SupplyDto {
    private Long id;
    private String supplyNumber;
    private String supplierName;
    private LocalDate supplyDate;
    private Double totalCost;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime receivedAt;
}