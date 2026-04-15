package ru.car.entity.claim;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Table(name = "claims", schema = "claim")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClaimEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "claim_number", unique = true)
    private String claimNumber;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(name = "vehicle_id", nullable = false)
    private Long vehicleId;

    @Column(name = "master_id")
    private Long masterId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ClaimStatus status = ClaimStatus.CREATED;

    @Column(name = "problem_description", nullable = false, columnDefinition = "TEXT")
    private String problemDescription;

    @Column(name = "customer_notes", columnDefinition = "TEXT")
    private String customerNotes;

    @Column(name = "internal_notes", columnDefinition = "TEXT")
    private String internalNotes;

    @Column(name = "mileage_at_entry", nullable = false)
    private Integer mileageAtEntry;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    private ClaimPriority priority = ClaimPriority.NORMAL;

    @Column(name = "is_approved")
    private Boolean isApproved;

    @Column(name = "is_paid")
    private Boolean isPaid;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "scheduled_date")
    private LocalDate scheduledDate;

    @Column(name = "diagnosis_date")
    private LocalDateTime diagnosisDate;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "completion_date")
    private LocalDateTime completionDate;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "claim", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ClaimWorkItemEntity> workItems = new ArrayList<>();

    @OneToMany(mappedBy = "claim", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ClaimPartEntity> usedParts = new ArrayList<>();


}
