package com.example.demo.controllers.retail;

import com.example.demo.dtos.retail.request.OrderRequestDto;
import com.example.demo.dtos.retail.response.OrderResponseDto;
import com.example.demo.services.retail.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody OrderRequestDto orderRequestDto) {
        OrderResponseDto order = orderService.createOrder(orderRequestDto);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }
}
