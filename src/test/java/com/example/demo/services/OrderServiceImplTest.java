package com.example.demo.services;

import com.example.demo.dtos.request.OrderRequestDto;
import com.example.demo.dtos.response.OrderResponseDto;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.mappers.OrderMapper;
import com.example.demo.models.Order;
import com.example.demo.models.Product;
import com.example.demo.models.User;
import com.example.demo.repositories.OrderRepository;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void createOrder_Success_ReturnsOrderDto() {
        OrderRequestDto requestDto = new OrderRequestDto();
        requestDto.setUserId(1L);
        requestDto.setProducts(Map.of(100L, 2));

        User mockUser = new User();
        mockUser.setId(1L);

        Product mockProduct = new Product();
        mockProduct.setId(100L);
        mockProduct.setStockQuantity(2);

        Order savedOrder = new Order();
        savedOrder.setId(5L);

        OrderResponseDto responseDto = new OrderResponseDto();
        responseDto.setId(5L);
        responseDto.setTotalAmount(BigDecimal.valueOf(100.0));

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(productRepository.findById(100L)).thenReturn(Optional.of(mockProduct));

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(orderMapper.toResponseDto(savedOrder)).thenReturn(responseDto);

        OrderResponseDto result = orderService.createOrder(requestDto);

        assertNotNull(result);
        assertEquals(5L, result.getId());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void createOrder_UserNotFound_ThrowsException() {
        OrderRequestDto requestDto = new OrderRequestDto();
        requestDto.setUserId(99L);
        requestDto.setProducts(Map.of(100L, 1));

        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            orderService.createOrder(requestDto);
        });

        verify(orderRepository, never()).save(any());
        verify(productRepository, never()).findById(any());
    }

    @Test
    void createOrder_ProductNotFound_ThrowsException() {
        OrderRequestDto requestDto = new OrderRequestDto();
        requestDto.setUserId(1L);
        requestDto.setProducts(Map.of(999L, 1));

        User mockUser = new User();
        mockUser.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            orderService.createOrder(requestDto);
        });

        verify(orderRepository, never()).save(any());
    }

    @Test
    void createOrder_InsufficientStock_ThrowsException() {
        OrderRequestDto requestDto = new OrderRequestDto();
        requestDto.setUserId(1L);
        requestDto.setProducts(Map.of(100L, 5));

        User mockUser = new User();
        mockUser.setId(1L);

        Product mockProduct = new Product();
        mockProduct.setId(100L);
        mockProduct.setStockQuantity(2);

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(productRepository.findById(100L)).thenReturn(Optional.of(mockProduct));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            orderService.createOrder(requestDto);
        });

        verify(orderRepository, never()).save(any());
    }
}
