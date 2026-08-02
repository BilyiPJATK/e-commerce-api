package com.example.demo.mappers;

import com.example.demo.dtos.retail.request.ProductRequestDto;
import com.example.demo.dtos.retail.response.ProductResponseDto;
import com.example.demo.models.retail.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    Product toEntity(ProductRequestDto dto);
    ProductResponseDto toResponseDto(Product entity);
}
