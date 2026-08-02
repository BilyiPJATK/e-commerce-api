package com.example.demo.services;


import com.example.demo.dtos.retail.request.ProductRequestDto;
import com.example.demo.dtos.retail.response.ProductResponseDto;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.mappers.ProductMapper;
import com.example.demo.models.retail.Product;
import com.example.demo.repositories.retail.ProductRepository;
import com.example.demo.services.retail.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void getProductById_Success_ReturnsProductDto() {
        Long productId = 1L;
        Product fakeProduct = new Product();
        fakeProduct.setId(productId);
        fakeProduct.setName("Test Product");

        ProductResponseDto fakeDto = new ProductResponseDto();
        fakeDto.setName("Test Product");

        when(productRepository.findById(productId)).thenReturn(Optional.of(fakeProduct));
        when(productMapper.toResponseDto(fakeProduct)).thenReturn(fakeDto);

        ProductResponseDto result = productService.getProductById(productId);

        assertNotNull(result);
        assertEquals("Test Product", result.getName());

        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void getProductById_ProductNotFound_ThrowsException(){
        Long productId = 99L;

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            productService.getProductById(productId);
        });

        verify(productMapper, never()).toResponseDto(any());
    }

    @Test
    void addProduct_Success_ReturnsSavedProductDto() {
        ProductRequestDto requestDto = new ProductRequestDto();
        requestDto.setName("New Headset");
        requestDto.setPrice(BigDecimal.valueOf(150.00));

        Product mappedEntity = new Product();
        mappedEntity.setName("New Headset");
        mappedEntity.setPrice(BigDecimal.valueOf(150.00));

        Product savedEntity = new Product();
        savedEntity.setId(5L);
        savedEntity.setName("New Headset");
        savedEntity.setPrice(BigDecimal.valueOf(150.00));

        ProductResponseDto expectedResponse = new ProductResponseDto();
        expectedResponse.setId(5L);
        expectedResponse.setName("New Headset");

        when(productMapper.toEntity(requestDto)).thenReturn(mappedEntity);
        when(productRepository.save(mappedEntity)).thenReturn(savedEntity);
        when(productMapper.toResponseDto(savedEntity)).thenReturn(expectedResponse);

        ProductResponseDto result = productService.addProduct(requestDto);

        assertNotNull(result);
        assertEquals(5L, result.getId());
        assertEquals("New Headset", result.getName());

        verify(productRepository, times(1)).save(mappedEntity);
    }

    @Test
    void getAllProducts_ReturnsPaginatedProducts() {
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        Product product = new Product();
        product.setId(1L);
        product.setName("Gaming Mouse");

        Page<Product> productPage = new PageImpl<>(List.of(product));

        ProductResponseDto responseDto = new ProductResponseDto();
        responseDto.setId(1L);
        responseDto.setName("Gaming Mouse");

        when(productRepository.findAll(pageable)).thenReturn(productPage);
        when(productMapper.toResponseDto(product)).thenReturn(responseDto);

        Page<ProductResponseDto> result = productService.getAllProducts(page, size);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Gaming Mouse", result.getContent().get(0).getName());

        verify(productRepository, times(1)).findAll(pageable);
    }

    @Test
    void getAllProducts_EmptyDatabase_ReturnsEmptyPage() {
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        Page<Product> emptyPage = Page.empty();

        when(productRepository.findAll(pageable)).thenReturn(emptyPage);

        Page<ProductResponseDto> result = productService.getAllProducts(page, size);

        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());

        verify(productMapper, never()).toResponseDto(any());
    }
}
