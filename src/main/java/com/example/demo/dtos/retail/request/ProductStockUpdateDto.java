package com.example.demo.dtos.retail.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductStockUpdateDto {
    @NotNull(message = "Quantity change is required")
    @Min(0)
    private Integer quantity;
}