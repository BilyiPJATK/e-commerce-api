package com.example.demo.dtos.rental.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class MaintenanceCompleteRequestDto {
    private BigDecimal finalCost;
}
