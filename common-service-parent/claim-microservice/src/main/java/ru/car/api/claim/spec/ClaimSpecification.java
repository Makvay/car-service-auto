package ru.car.api.claim.spec;

import org.springframework.data.jpa.domain.Specification;
import ru.car.entity.claim.ClaimEntity;
import ru.car.entity.claim.ClaimPriority;
import ru.car.entity.claim.ClaimStatus;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public final class ClaimSpecification {

    private ClaimSpecification() {}

    public static Specification<ClaimEntity> withFilters(
            Long clientId,
            Long vehicleId,
            Long masterId,
            List<ClaimStatus> statuses,
            ClaimPriority priority) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Фильтр по клиенту
            if (clientId != null) {
                predicates.add(cb.equal(root.get("clientId"), clientId));
            }

            // Фильтр по автомобилю
            if (vehicleId != null) {
                predicates.add(cb.equal(root.get("vehicleId"), vehicleId));
            }

            // Фильтр по мастеру
            if (masterId != null) {
                predicates.add(cb.equal(root.get("masterId"), masterId));
            }

            // Фильтр по статусу
            if (statuses != null && !statuses.isEmpty()) {
                predicates.add(root.get("status").in(statuses));
            }

            // Фильтр по приоритету
            if (priority != null) {
                predicates.add(cb.equal(root.get("priority"), priority));
            }

            // возвращаем объединенный предикат
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}