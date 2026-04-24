package ru.car.entity.warehouse;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reservations", schema = "warehouse")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reservation_number", nullable = false, unique = true, length = 50)
    private String reservationNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_part", nullable = false)
    private PartEntity part;

    @Column(name = "fk_claim", nullable = false)
    private Long claimId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "status", length = 20)
    private String status = "RESERVED";

    @Column(name = "reserved_by")
    private Long reservedBy;

    @Column(name = "reserved_at")
    private LocalDateTime reservedAt;

    @Column(name = "used_at")
    private LocalDateTime usedAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "notes")
    private String notes;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @PrePersist
    void prePersist() {
        if (reservationNumber == null || reservationNumber.isBlank()) {
            reservationNumber = "RSV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }
        if (status == null || status.isBlank()) {
            status = "RESERVED";
        }
        if (reservedAt == null) {
            reservedAt = LocalDateTime.now();
        }
    }
}
