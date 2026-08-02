package com.example.demo.mappers;

import com.example.demo.dtos.retail.response.OrderItemResponseDto;
import com.example.demo.dtos.retail.response.OrderResponseDto;
import com.example.demo.models.retail.Order;
import com.example.demo.models.retail.OrderItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.math.BigDecimal;

@Mapper(componentModel = "spring", uses = {UserMapper.class, ProductMapper.class})
public interface OrderMapper {

    @Mapping(target = "totalAmount", ignore = true)
    OrderResponseDto toResponseDto(Order entity);

    OrderItemResponseDto toOrderItemResponseDto(OrderItem item);

    @AfterMapping
    default void calculateTotalAmount(Order entity, @MappingTarget OrderResponseDto dto) {
        if (entity.getItems() != null) {
            BigDecimal totalAmount = entity.getItems().stream()
                    .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            dto.setTotalAmount(totalAmount);
        } else {
            dto.setTotalAmount(BigDecimal.ZERO);
        }
    }
}