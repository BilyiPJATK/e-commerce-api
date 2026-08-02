package com.example.demo.services.rental.impl;

import com.example.demo.dtos.rental.request.EquipmentRequestDto;
import com.example.demo.dtos.rental.response.EquipmentResponseDto;
import com.example.demo.enums.EquipmentCondition;
import com.example.demo.enums.EquipmentType;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.mappers.EquipmentMapper;
import com.example.demo.models.rental.Equipment;
import com.example.demo.repositories.rental.EquipmentRepository;
import com.example.demo.services.rental.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final EquipmentMapper equipmentMapper;

    @Override
    @Transactional
    public EquipmentResponseDto addEquipment(EquipmentRequestDto equipmentDto) {
        Equipment equipment = equipmentMapper.toEntity(equipmentDto);

        if (equipment.getCondition() == null) {
            equipment.setCondition(EquipmentCondition.NEW);
        }

        Equipment savedEquipment = equipmentRepository.save(equipment);
        return equipmentMapper.toResponseDto(savedEquipment);
    }

    @Override
    public EquipmentResponseDto getEquipmentById(Long id) {
        Equipment equipment = getEquipmentEntityById(id);
        return equipmentMapper.toResponseDto(equipment);
    }

    @Override
    public List<EquipmentResponseDto> getAllEquipment() {
        return equipmentRepository.findAll().stream()
                .map(equipmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EquipmentResponseDto> getEquipmentByType(EquipmentType type) {
        return equipmentRepository.findByType(type).stream()
                .map(equipmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<EquipmentResponseDto> getEquipmentByCondition(EquipmentCondition condition) {
        return equipmentRepository.findByCondition(condition).stream()
                .map(equipmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EquipmentResponseDto updateEquipmentCondition(Long id, EquipmentCondition newCondition) {
        Equipment equipment = getEquipmentEntityById(id);
        equipment.setCondition(newCondition);
        Equipment updatedEquipment = equipmentRepository.save(equipment);
        return equipmentMapper.toResponseDto(updatedEquipment);
    }

    @Override
    @Transactional
    public void retireEquipment(Long id) {
        Equipment equipment = getEquipmentEntityById(id);
        equipment.setCondition(EquipmentCondition.RETIRED);
        equipmentRepository.save(equipment);
    }

    private Equipment getEquipmentEntityById(Long id) {
        return equipmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with ID: " + id));
    }
}