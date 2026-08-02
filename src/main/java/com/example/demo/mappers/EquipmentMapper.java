package com.example.demo.mappers;

import com.example.demo.dtos.rental.request.EquipmentRequestDto;
import com.example.demo.dtos.rental.response.EquipmentResponseDto;
import com.example.demo.models.rental.Equipment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EquipmentMapper {
    Equipment toEntity(EquipmentRequestDto dto);
    EquipmentResponseDto toResponseDto(Equipment entity);
}