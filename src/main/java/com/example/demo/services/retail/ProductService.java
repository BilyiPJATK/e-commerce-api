package com.example.demo.services.retail;

import com.example.demo.dtos.retail.request.ProductRequestDto;
import com.example.demo.dtos.retail.request.ProductStockUpdateDto;
import com.example.demo.dtos.retail.response.ProductResponseDto;
import org.springframework.data.domain.Page;

public interface ProductService {
    ProductResponseDto addProduct(ProductRequestDto requestDto);
    Page<ProductResponseDto> getAllProducts(int page, int size);
    ProductResponseDto getProductById(Long id);
    ProductResponseDto updateProduct(Long id, ProductRequestDto requestDto);
    ProductResponseDto updateStock(Long id, ProductStockUpdateDto requestDto);
    void deleteProduct(Long id);
}
