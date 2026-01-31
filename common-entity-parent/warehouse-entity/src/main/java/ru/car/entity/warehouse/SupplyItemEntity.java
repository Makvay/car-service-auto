package ru.car.entity.warehouse;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "supply_items", schema = "warehouse")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplyItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "supply_id", nullable = false)
    private Long supplyId;

    @Column(name = "part_id", nullable = false)
    private Long partId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unit_cost")
    private Double unitCost;

    @Column(name = "total_cost")
    private Double totalCost;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}