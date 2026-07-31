package com.example.demo.controllers;

import com.example.demo.dtos.request.ProductRequestDto;
import com.example.demo.dtos.response.ProductResponseDto;
import com.example.demo.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Manually instantiated because Spring Boot 4.1 @WebMvcTest does not autowire ObjectMapper by default
    private ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private ProductService productService;

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
