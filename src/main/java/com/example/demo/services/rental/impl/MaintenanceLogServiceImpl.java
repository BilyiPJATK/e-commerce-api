package com.example.demo.services.rental.impl;

import com.example.demo.dtos.rental.request.MaintenanceLogRequestDto;
import com.example.demo.dtos.rental.response.MaintenanceLogResponseDto;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.mappers.MaintenanceLogMapper;
import com.example.demo.models.rental.Equipment;
import com.example.demo.models.rental.MaintenanceLog;
import com.example.demo.repositories.rental.EquipmentRepository;
import com.example.demo.repositories.rental.MaintenanceLogRepository;
import com.example.demo.services.rental.MaintenanceLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MaintenanceLogServiceImpl implements MaintenanceLogService {

    private final MaintenanceLogRepository maintenanceLogRepository;
    private final EquipmentRepository equipmentRepository;
    private final MaintenanceLogMapper maintenanceLogMapper;

    @Override
    @Transactional
    public MaintenanceLogResponseDto createMaintenanceLog(MaintenanceLogRequestDto requestDto) {
        Equipment equipment = equipmentRepository.findById(requestDto.getEquipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with ID: " + requestDto.getEquipmentId()));

        MaintenanceLog log = maintenanceLogMapper.toEntity(requestDto);
        log.setEquipment(equipment);

        MaintenanceLog savedLog = maintenanceLogRepository.save(log);
        return maintenanceLogMapper.toResponseDto(savedLog);
    }

    @Override
    public MaintenanceLogResponseDto getMaintenanceLogById(Long id) {
        MaintenanceLog log = maintenanceLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance log not found with ID: " + id));
        return maintenanceLogMapper.toResponseDto(log);
    }

    @Override
    public List<MaintenanceLogResponseDto> getLogsByEquipmentId(Long equipmentId) {
        if (!equipmentRepository.existsById(equipmentId)) {
            throw new ResourceNotFoundException("Equipment not found with ID: " + equipmentId);
        }

        return maintenanceLogRepository.findByEquipmentId(equipmentId).stream()
                .map(maintenanceLogMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaintenanceLogResponseDto> getAllMaintenanceLogs() {
        return maintenanceLogRepository.findAll().stream()
                .map(maintenanceLogMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteMaintenanceLog(Long id) {
        if (!maintenanceLogRepository.existsById(id)) {
            throw new ResourceNotFoundException("Maintenance log not found with ID: " + id);
        }
        maintenanceLogRepository.deleteById(id);
    }
}