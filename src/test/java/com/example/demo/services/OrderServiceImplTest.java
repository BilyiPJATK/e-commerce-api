package com.example.demo.services;

import com.example.demo.dtos.retail.request.OrderRequestDto;
import com.example.demo.dtos.retail.response.OrderResponseDto;
import com.example.demo.enums.OrderStatus;
import com.example.demo.exceptions.InsufficientStockException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.mappers.OrderMapper;
import com.example.demo.models.retail.Order;
import com.example.demo.models.retail.Product;
import com.example.demo.models.users.User;
import com.example.demo.repositories.retail.OrderRepository;
import com.example.demo.repositories.retail.ProductRepository;
import com.example.demo.repositories.users.UserRepository;
import com.example.demo.services.retail.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
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
        mockProduct.setPrice(BigDecimal.valueOf(50.0));

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

        assertThrows(ResourceNotFoundException.class, () -> orderService.createOrder(requestDto));

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

        assertThrows(ResourceNotFoundException.class, () -> orderService.createOrder(requestDto));

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

        assertThrows(InsufficientStockException.class, () -> orderService.createOrder(requestDto));

        verify(orderRepository, never()).save(any());
    }

    @Test
    void getAllOrders_Success_ReturnsList() {
        Order order = new Order();
        OrderResponseDto dto = new OrderResponseDto();

        when(orderRepository.findAll()).thenReturn(List.of(order));
        when(orderMapper.toResponseDto(order)).thenReturn(dto);

        List<OrderResponseDto> results = orderService.getAllOrders();

        assertEquals(1, results.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void getOrderById_Success_ReturnsOrder() {
        Order order = new Order();
        order.setId(1L);
        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(1L);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderMapper.toResponseDto(order)).thenReturn(dto);

        OrderResponseDto result = orderService.getOrderById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getOrderById_NotFound_ThrowsException() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrderById(99L));
    }

    @Test
    void getOrdersByUserId_Success_ReturnsList() {
        Order order = new Order();
        OrderResponseDto dto = new OrderResponseDto();

        when(userRepository.existsById(1L)).thenReturn(true);
        when(orderRepository.findByUserId(1L)).thenReturn(List.of(order));
        when(orderMapper.toResponseDto(order)).thenReturn(dto);

        List<OrderResponseDto> results = orderService.getOrdersByUserId(1L);

        assertEquals(1, results.size());
    }

    @Test
    void getOrdersByUserId_UserNotFound_ThrowsException() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> orderService.getOrdersByUserId(99L));
        verify(orderRepository, never()).findByUserId(any());
    }

    @Test
    void updateOrderStatus_Success_ReturnsUpdatedOrder() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.PENDING);

        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(1L);
        dto.setStatus(OrderStatus.PAID);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toResponseDto(order)).thenReturn(dto);

        OrderResponseDto result = orderService.updateOrderStatus(1L, OrderStatus.PAID);

        assertNotNull(result);
        assertEquals(OrderStatus.PAID, result.getStatus());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void updateOrderStatus_OrderNotFound_ThrowsException() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.updateOrderStatus(99L, OrderStatus.PAID));
        verify(orderRepository, never()).save(any());
    }
}