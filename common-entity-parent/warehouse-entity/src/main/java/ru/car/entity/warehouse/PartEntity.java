package ru.car.entity.warehouse;

import jakarta.persistence.*;
import lombok.*;
import ru.car.dto.warehouse.MeasurementUnit;
import ru.car.entity.nsi.PartCategoryEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "parts", schema = "warehouse")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sku", nullable = false, unique = true, length = 100)
    private String sku;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_part_id")
    private PartEntity part;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_category", nullable = false)
    private PartCategoryEntity category;

    @Column(name = "brand", length = 100)
    private String brand;

    @Column(name = "unit_price", nullable = false)
    private Double unitPrice;

    @Column(name = "cost_price", nullable = false)
    private Double costPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "measurement_unit", length = 20)
    private MeasurementUnit measurementUnit = MeasurementUnit.PCS;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id")
    private List<InventoryEntity> inventory = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "part_id")
    private List<ReservationEntity> reservations = new ArrayList<>();
}