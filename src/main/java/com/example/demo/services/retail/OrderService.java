package com.example.demo.services.retail;

import com.example.demo.dtos.retail.request.OrderRequestDto;
import com.example.demo.dtos.retail.response.OrderResponseDto;
import com.example.demo.enums.OrderStatus;

import java.util.List;

public interface OrderService {
    OrderResponseDto createOrder(OrderRequestDto requestDto);

    List<OrderResponseDto> getAllOrders();

    OrderResponseDto getOrderById(Long id);

    List<OrderResponseDto> getOrdersByUserId(Long userId);

    OrderResponseDto updateOrderStatus(Long id, OrderStatus newStatus);
}
