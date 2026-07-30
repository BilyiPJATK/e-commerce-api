package com.example.demo.mappers;

import com.example.demo.dtos.request.OrderRequestDto;
import com.example.demo.dtos.response.OrderItemResponseDto;
import com.example.demo.dtos.response.OrderResponseDto;
import com.example.demo.models.Order;
import com.example.demo.models.OrderItem;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(target = "totalAmount", ignore = true)
    OrderResponseDto toResponseDto(Order entity);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "price", target = "priceAtPurchase")
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
