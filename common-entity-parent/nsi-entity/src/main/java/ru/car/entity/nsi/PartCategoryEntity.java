package ru.car.entity.nsi;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
@Setter
@Getter
@Entity
@Table(name = "part_categories", schema = "nsi")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartCategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}