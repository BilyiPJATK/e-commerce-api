package com.example.demo.mappers;

import com.example.demo.dtos.rental.response.RentalTransactionResponseDto;
import com.example.demo.models.rental.RentalTransaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {MemberMapper.class, EquipmentMapper.class})
public interface RentalTransactionMapper {
    RentalTransactionResponseDto toResponseDto(RentalTransaction entity);
}