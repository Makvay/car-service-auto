package ru.car.entity.claim;

import jakarta.persistence.*;
import lombok.Data;
import ru.car.entity.warehouse.PartEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "claim_parts", schema = "claim")
@Data
public class ClaimPartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_claim", nullable = false)
    private ClaimEntity claim;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_part", nullable = false)
    private PartEntity part;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unit_price")
    private Double unitPrice;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}