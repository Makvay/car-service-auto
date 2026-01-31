package ru.car.dto.master;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class WorkScheduleDto {
    private Long id;
    private MasterDto master;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private DayType dayType;
    private LocalDateTime createdAt;
}