package ru.car.dto.claim;

import lombok.Data;


import java.time.LocalDateTime;

@Data
public class ClaimPartDto {
    private Long id;
    private ClaimDto claim;
    private Long partId;
    private String partCode;
    private String partName;
    private String partCategory;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
    private LocalDateTime createdAt;
}