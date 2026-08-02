package com.example.demo.mappers;

import com.example.demo.dtos.rental.request.MaintenanceLogRequestDto;
import com.example.demo.dtos.rental.response.MaintenanceLogResponseDto;
import com.example.demo.models.rental.MaintenanceLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {EquipmentMapper.class})
public interface MaintenanceLogMapper {

    @Mapping(target = "equipment", ignore = true)
    MaintenanceLog toEntity(MaintenanceLogRequestDto dto);

    MaintenanceLogResponseDto toResponseDto(MaintenanceLog entity);
}