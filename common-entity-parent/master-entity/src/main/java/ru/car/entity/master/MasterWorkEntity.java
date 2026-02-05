package ru.car.entity.master;

import jakarta.persistence.*;
import lombok.*;


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

    // ЗАМЕНЯЕМ ClaimEntity на Long
    @Column(name = "claim_id", nullable = false)
    private Long claimId;

    // ЗАМЕНЯЕМ
    // ServiceEntity на Long
    @Column(name = "service_id", nullable = false)
    private Long serviceId;

    @Column(name = "service_name", length = 200)
    private String serviceName; // Копия названия услуги

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

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}