package ru.car.entity.master;

import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "work_schedule", schema = "master")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkScheduleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_master", nullable = false)
    private MasterEntity master;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;


    @Enumerated(EnumType.STRING)
    @Column(name = "day_type", length = 20)
    private DayType dayType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}