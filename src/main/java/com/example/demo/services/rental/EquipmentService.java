package com.example.demo.services.rental;

import com.example.demo.dtos.rental.request.EquipmentRequestDto;
import com.example.demo.dtos.rental.response.EquipmentResponseDto;
import com.example.demo.enums.EquipmentCondition;
import com.example.demo.enums.EquipmentType;

import java.util.List;

public interface EquipmentService {
    EquipmentResponseDto addEquipment(EquipmentRequestDto equipmentDto);
    EquipmentResponseDto getEquipmentById(Long id);
    List<EquipmentResponseDto> getAllEquipment();
    List<EquipmentResponseDto> getEquipmentByType(EquipmentType type);
    List<EquipmentResponseDto> getEquipmentByCondition(EquipmentCondition condition);
    EquipmentResponseDto updateEquipmentCondition(Long id, EquipmentCondition newCondition);
    void retireEquipment(Long id);
}
