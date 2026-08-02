package com.example.demo.services;

import com.example.demo.dtos.rental.request.MaintenanceLogRequestDto;
import com.example.demo.dtos.rental.response.MaintenanceLogResponseDto;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.mappers.MaintenanceLogMapper;
import com.example.demo.models.rental.Equipment;
import com.example.demo.models.rental.MaintenanceLog;
import com.example.demo.repositories.rental.EquipmentRepository;
import com.example.demo.repositories.rental.MaintenanceLogRepository;
import com.example.demo.services.rental.impl.MaintenanceLogServiceImpl;
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
class MaintenanceLogServiceImplTest {

    @Mock
    private MaintenanceLogRepository maintenanceLogRepository;

    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock
    private MaintenanceLogMapper maintenanceLogMapper;

    @InjectMocks
    private MaintenanceLogServiceImpl maintenanceLogService;

    @Test
    void createMaintenanceLog_Success_ReturnsSavedLog() {
        MaintenanceLogRequestDto requestDto = new MaintenanceLogRequestDto();
        requestDto.setEquipmentId(1L);

        Equipment equipment = new Equipment();
        equipment.setId(1L);

        MaintenanceLog logEntity = new MaintenanceLog();
        MaintenanceLog savedLog = new MaintenanceLog();
        savedLog.setId(1L);

        MaintenanceLogResponseDto responseDto = new MaintenanceLogResponseDto();
        responseDto.setId(1L);

        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));
        when(maintenanceLogMapper.toEntity(requestDto)).thenReturn(logEntity);
        when(maintenanceLogRepository.save(logEntity)).thenReturn(savedLog);
        when(maintenanceLogMapper.toResponseDto(savedLog)).thenReturn(responseDto);

        MaintenanceLogResponseDto result = maintenanceLogService.createMaintenanceLog(requestDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(equipment, logEntity.getEquipment());
    }

    @Test
    void createMaintenanceLog_EquipmentNotFound_ThrowsException() {
        MaintenanceLogRequestDto requestDto = new MaintenanceLogRequestDto();
        requestDto.setEquipmentId(99L);

        when(equipmentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> maintenanceLogService.createMaintenanceLog(requestDto));
        verify(maintenanceLogRepository, never()).save(any());
    }

    @Test
    void deleteMaintenanceLog_Success_DeletesLog() {
        when(maintenanceLogRepository.existsById(1L)).thenReturn(true);
        doNothing().when(maintenanceLogRepository).deleteById(1L);

        maintenanceLogService.deleteMaintenanceLog(1L);

        verify(maintenanceLogRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteMaintenanceLog_NotFound_ThrowsException() {
        when(maintenanceLogRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> maintenanceLogService.deleteMaintenanceLog(99L));
    }
}