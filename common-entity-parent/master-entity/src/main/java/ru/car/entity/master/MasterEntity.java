package ru.car.entity.master;

import jakarta.persistence.*;
import lombok.*;
import ru.car.entity.master.MasterQualification;
import ru.car.entity.master.MasterSpecialization;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "masters", schema = "master")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MasterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "employee_code", nullable = false, unique = true, length = 50)
    private String employeeCode;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "phone", nullable = false, unique = true, length = 20)
    private String phone;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "specialization", nullable = false, length = 100)
    private MasterSpecialization specialization;

    @Enumerated(EnumType.STRING)
    @Column(name = "qualification_level", length = 50)
    private MasterQualification qualificationLevel;

    @Column(name = "hourly_rate", nullable = false)
    private Double hourlyRate;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @OneToMany(mappedBy = "master", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<WorkScheduleEntity> workSchedules = new ArrayList<>();

    @OneToMany(mappedBy = "master", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MasterWorkEntity> works = new ArrayList<>();
}