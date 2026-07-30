package com.example.demo.services;

import com.example.demo.dtos.request.ProductRequestDto;
import com.example.demo.dtos.response.ProductResponseDto;
import com.example.demo.models.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    ProductResponseDto addProduct(ProductRequestDto requestDto);
    Page<ProductResponseDto> getAllProducts(int page, int size);
    ProductResponseDto getProductById(Long id);
}
