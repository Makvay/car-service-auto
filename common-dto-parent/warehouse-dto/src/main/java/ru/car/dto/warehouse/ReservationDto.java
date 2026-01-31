package ru.car.dto.warehouse;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReservationDto {
    private Long id;
    private String reservationNumber;
    private Long claimId;
    private Long partId;
    private Integer quantity;
    private String status;
    private Long reservedBy;
    private LocalDateTime reservedAt;
    private LocalDateTime usedAt;
    private LocalDateTime cancelledAt;
    private String notes;
}