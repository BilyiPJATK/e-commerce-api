package com.example.demo.services;

import com.example.demo.dtos.request.OrderRequestDto;
import com.example.demo.dtos.response.OrderResponseDto;
import com.example.demo.models.Order;
import java.util.Map;

public interface OrderService {
    OrderResponseDto createOrder(OrderRequestDto requestDto);
}
