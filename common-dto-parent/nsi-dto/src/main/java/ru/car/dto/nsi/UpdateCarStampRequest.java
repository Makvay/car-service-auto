package ru.car.dto.nsi;

import lombok.Data;

@Data
public class UpdateCarStampRequest {
    private String name;
    private String country;
    private Boolean isActive;
}