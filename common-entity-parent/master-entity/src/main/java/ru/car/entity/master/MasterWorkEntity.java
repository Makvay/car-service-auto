package ru.car.entity.master;

import jakarta.persistence.*;
import lombok.*;
import ru.car.entity.claim.ClaimEntity;
import ru.car.entity.nsi.ServiceEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "master_work", schema = "master")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MasterWorkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_master", nullable = false)
    private MasterEntity master;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_claim", nullable = false)
    private ClaimEntity claim;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_service")
    private ServiceEntity service;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "hours_worked")
    private Double hoursWorked;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}