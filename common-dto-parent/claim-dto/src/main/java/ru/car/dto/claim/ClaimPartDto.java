package ru.car.dto.claim;

import lombok.Data;
import ru.car.dto.warehouse.PartDto;

import java.time.LocalDateTime;

@Data
public class ClaimPartDto {
    private Long id;
    private ClaimDto claim;
    private PartDto part;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
    private LocalDateTime createdAt;
}