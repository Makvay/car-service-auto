package ru.car.entity.master;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "master_specializations", schema = "master")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MasterSpecializationEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "master_id", nullable = false)
    private Long masterId;

    @Column(name = "specialization", nullable = false, length = 50)
    private String specialization;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}