package com.example.demo.dtos.rental.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MaintenanceLogRequestDto {
    private LocalDate dateSent;
    private LocalDate dateReturned;
    private BigDecimal cost;
    private String description;
    private Long equipmentId;
}