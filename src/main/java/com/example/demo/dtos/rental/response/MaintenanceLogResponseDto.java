package com.example.demo.dtos.rental.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MaintenanceLogResponseDto {
    private Long id;
    private LocalDate dateSent;
    private LocalDate dateReturned;
    private BigDecimal cost;
    private String description;
    private EquipmentResponseDto equipment;
}