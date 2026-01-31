package ru.car.entity.client;

import jakarta.persistence.*;
import lombok.Data;
import ru.car.entity.nsi.CarStampEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "client_car", schema = "client")
@Data
public class ClientCarEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_client", nullable = false)
    private ClientEntity client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_car_stamp")
    private CarStampEntity carStamp;

    @Column(name = "model", length = 100)
    private String model;

    @Column(name = "vin", nullable = false, unique = true, length = 17)
    private String vin;

    @Column(name = "license_plate", unique = true, length = 20)
    private String licensePlate;

    @Column(name = "year")
    private Integer year;

    @Column(name = "mileage")
    private Integer mileage;

    @Column(name = "color", length = 50)
    private String color;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Column(name = "notes", length = 2000)
    private String notes;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}