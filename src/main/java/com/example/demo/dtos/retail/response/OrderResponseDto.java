package com.example.demo.dtos.retail.response;

import com.example.demo.dtos.users.response.UserResponseDto;
import com.example.demo.enums.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDto {
    private Long id;
    private UserResponseDto user;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private List<OrderItemResponseDto> items;
    private BigDecimal totalAmount;
}
