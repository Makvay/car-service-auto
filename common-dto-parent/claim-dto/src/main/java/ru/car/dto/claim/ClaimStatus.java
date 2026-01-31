package ru.car.dto.claim;

import lombok.Getter;

@Getter
public enum ClaimStatus {
    CREATED("Создана"),
    WAITING_DIAGNOSIS("Ожидает диагностики"),
    DIAGNOSIS_IN_PROGRESS("Диагностика"),
    WAITING_APPROVAL("Ожидает согласования"),
    APPROVED("Согласована"),
    REJECTED("Отклонена"),
    WAITING_PARTS("Ожидает запчастей"),
    IN_PROGRESS("В работе"),
    WAITING_PAYMENT("Ожидает оплаты"),
    PAID("Оплачена"),
    COMPLETED("Завершена"),
    CANCELLED("Отменена");

    private final String description;

    ClaimStatus(String description) {
        this.description = description;
    }
}