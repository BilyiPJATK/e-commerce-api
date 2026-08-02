package com.example.demo.dtos.retail.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

@Data
public class OrderRequestDto {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotEmpty(message = "List of products cant be empty")
    private Map<Long, Integer> products;
}
