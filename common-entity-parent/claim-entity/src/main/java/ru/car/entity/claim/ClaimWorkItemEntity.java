package ru.car.entity.claim;

import jakarta.persistence.*;
import lombok.Data;
import ru.car.entity.nsi.ServiceEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "claim_work_items", schema = "claim")
@Data
public class ClaimWorkItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_claim", nullable = false)
    private ClaimEntity claim;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_service")
    private ServiceEntity service;

    @Column(name = "description")
    private String description;

    @Column(name = "quantity")
    private Integer quantity = 1;

    @Column(name = "unit_price")
    private Double unitPrice;

    @Column(name = "total_price")
    private Double totalPrice;

    @Column(name = "is_completed")
    private Boolean isCompleted;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}