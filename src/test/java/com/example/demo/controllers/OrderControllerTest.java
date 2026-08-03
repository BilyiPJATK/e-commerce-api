package com.example.demo.controllers;

import com.example.demo.controllers.retail.OrderController;
import com.example.demo.dtos.retail.request.OrderRequestDto;
import com.example.demo.dtos.retail.response.OrderResponseDto;
import com.example.demo.enums.OrderStatus;
import com.example.demo.security.JwtService;
import com.example.demo.services.retail.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Manually instantiated because Spring Boot 4.1 @WebMvcTest does not autowire ObjectMapper by default
    private ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    void createOrder_ValidRequest_Returns201Created() throws Exception {
        OrderRequestDto request = new OrderRequestDto();
        request.setUserId(1L);
        request.setProducts(Map.of(100L, 2, 105L, 1));

        OrderResponseDto response = new OrderResponseDto();
        response.setId(5L);
        response.setTotalAmount(BigDecimal.valueOf(200.50));

        when(orderService.createOrder(any(OrderRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.totalAmount").value(200.50));
    }

    @Test
    void createOrder_MissingUserId_Returns400BadRequest() throws Exception {
        OrderRequestDto request = new OrderRequestDto();
        request.setUserId(null);
        request.setProducts(Map.of(100L, 2));

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createOrder_EmptyProducts_Returns400BadRequest() throws Exception {
        OrderRequestDto request = new OrderRequestDto();
        request.setUserId(1L);
        request.setProducts(Collections.emptyMap());

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllOrders_Returns200Ok() throws Exception {
        OrderResponseDto response = new OrderResponseDto();
        response.setId(1L);

        when(orderService.getAllOrders()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void getOrderById_Returns200Ok() throws Exception {
        OrderResponseDto response = new OrderResponseDto();
        response.setId(1L);

        when(orderService.getOrderById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getOrdersByUserId_Returns200Ok() throws Exception {
        OrderResponseDto response = new OrderResponseDto();
        response.setId(1L);

        when(orderService.getOrdersByUserId(2L)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/orders/user/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void updateOrderStatus_Returns200Ok() throws Exception {
        OrderResponseDto response = new OrderResponseDto();
        response.setId(1L);
        response.setStatus(OrderStatus.PAID);

        when(orderService.updateOrderStatus(eq(1L), any(OrderStatus.class))).thenReturn(response);

        mockMvc.perform(patch("/api/orders/1/status")
                        .param("status", "PAID"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("PAID"));
    }
}