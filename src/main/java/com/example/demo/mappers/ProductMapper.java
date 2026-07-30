package com.example.demo.mappers;

import com.example.demo.dtos.request.ProductRequestDto;
import com.example.demo.dtos.response.ProductResponseDto;
import com.example.demo.models.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toEntity(ProductRequestDto dto);
    ProductResponseDto toResponseDto(Product entity);
}
