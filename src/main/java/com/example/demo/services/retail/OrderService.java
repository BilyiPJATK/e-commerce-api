package com.example.demo.services.retail;

import com.example.demo.dtos.retail.request.OrderRequestDto;
import com.example.demo.dtos.retail.response.OrderResponseDto;

public interface OrderService {
    OrderResponseDto createOrder(OrderRequestDto requestDto);
}
