package ru.car.entity.claim;

public enum ClaimStatus {
    CREATED,
    WAITING_DIAGNOSIS,
    DIAGNOSIS_IN_PROGRESS,
    WAITING_APPROVAL,
    APPROVED,
    REJECTED,
    WAITING_PARTS,
    IN_PROGRESS,
    WAITING_PAYMENT,
    PAID,
    COMPLETED,
    CANCELLED
}
