package com.example.demo.services;

import com.example.demo.dtos.rental.request.EquipmentRequestDto;
import com.example.demo.dtos.rental.response.EquipmentResponseDto;
import com.example.demo.enums.EquipmentCondition;
import com.example.demo.enums.EquipmentType;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.mappers.EquipmentMapper;
import com.example.demo.models.rental.Equipment;
import com.example.demo.repositories.rental.EquipmentRepository;
import com.example.demo.services.rental.impl.EquipmentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EquipmentServiceImplTest {

    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock
    private EquipmentMapper equipmentMapper;

    @InjectMocks
    private EquipmentServiceImpl equipmentService;

    @Test
    void addEquipment_NullCondition_SetsConditionToNewAndSaves() {
        EquipmentRequestDto requestDto = new EquipmentRequestDto();

        Equipment mappedEntity = new Equipment();
        Equipment savedEntity = new Equipment();
        savedEntity.setId(1L);
        savedEntity.setCondition(EquipmentCondition.NEW);

        EquipmentResponseDto expectedResponse = new EquipmentResponseDto();
        expectedResponse.setId(1L);
        expectedResponse.setCondition(EquipmentCondition.NEW);

        when(equipmentMapper.toEntity(requestDto)).thenReturn(mappedEntity);
        when(equipmentRepository.save(mappedEntity)).thenReturn(savedEntity);
        when(equipmentMapper.toResponseDto(savedEntity)).thenReturn(expectedResponse);

        EquipmentResponseDto result = equipmentService.addEquipment(requestDto);

        assertNotNull(result);
        assertEquals(EquipmentCondition.NEW, result.getCondition());
        assertEquals(EquipmentCondition.NEW, mappedEntity.getCondition());
        verify(equipmentRepository, times(1)).save(mappedEntity);
    }

    @Test
    void getEquipmentById_Success_ReturnsDto() {
        Equipment equipment = new Equipment();
        equipment.setId(1L);
        EquipmentResponseDto responseDto = new EquipmentResponseDto();
        responseDto.setId(1L);

        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));
        when(equipmentMapper.toResponseDto(equipment)).thenReturn(responseDto);

        EquipmentResponseDto result = equipmentService.getEquipmentById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void getEquipmentById_NotFound_ThrowsException() {
        when(equipmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> equipmentService.getEquipmentById(99L));
    }

    @Test
    void retireEquipment_Success_UpdatesConditionToRetired() {
        Equipment equipment = new Equipment();
        equipment.setId(1L);
        equipment.setCondition(EquipmentCondition.GOOD);

        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));
        when(equipmentRepository.save(any(Equipment.class))).thenReturn(equipment);

        equipmentService.retireEquipment(1L);

        assertEquals(EquipmentCondition.RETIRED, equipment.getCondition());
        verify(equipmentRepository, times(1)).save(equipment);
    }
}