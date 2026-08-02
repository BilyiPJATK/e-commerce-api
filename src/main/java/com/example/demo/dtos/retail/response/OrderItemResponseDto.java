package com.example.demo.dtos.retail.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemResponseDto {
    private Long productId;
    private ProductResponseDto product;
    private Integer quantity;
    private BigDecimal priceAtPurchase;
}
