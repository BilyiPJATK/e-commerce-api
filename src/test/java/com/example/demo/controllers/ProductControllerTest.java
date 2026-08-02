package com.example.demo.controllers;

import com.example.demo.controllers.retail.ProductController;
import com.example.demo.dtos.retail.request.ProductRequestDto;
import com.example.demo.dtos.retail.response.ProductResponseDto;
import com.example.demo.security.JwtService;
import com.example.demo.services.retail.ProductService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Manually instantiated because Spring Boot 4.1 @WebMvcTest does not autowire ObjectMapper by default
    private ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    void addProduct_ValidRequest_Returns201Created() throws Exception {
        ProductRequestDto request = new ProductRequestDto();
        request.setName("Mechanical Keyboard");
        request.setPrice(BigDecimal.valueOf(120.00));
        request.setStockQuantity(50);

        ProductResponseDto response = new ProductResponseDto();
        response.setId(1L);
        response.setName("Mechanical Keyboard");

        when(productService.addProduct(any(ProductRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Mechanical Keyboard"));
    }

    @Test
    void addProduct_NegativePrice_Returns400BadRequest() throws Exception {
        ProductRequestDto request = new ProductRequestDto();
        request.setName("Mechanical Keyboard");
        request.setPrice(BigDecimal.valueOf(-50.00));

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.price").exists());
    }
}