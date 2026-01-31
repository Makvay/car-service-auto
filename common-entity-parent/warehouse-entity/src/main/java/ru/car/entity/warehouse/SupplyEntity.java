package ru.car.entity.warehouse;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "supplies", schema = "warehouse")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "supply_number", nullable = false, unique = true, length = 50)
    private String supplyNumber;

    @Column(name = "supplier_name", length = 255)
    private String supplierName;

    @Column(name = "supply_date", nullable = false)
    private LocalDate supplyDate;

    @Column(name = "total_cost")
    private Double totalCost;

    @Column(name = "status", length = 20)
    private String status = "PENDING";

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "received_at")
    private LocalDateTime receivedAt;
}