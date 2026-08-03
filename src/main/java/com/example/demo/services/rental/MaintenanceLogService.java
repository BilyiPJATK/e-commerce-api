package com.example.demo.services.rental;

import com.example.demo.dtos.rental.request.MaintenanceCompleteRequestDto;
import com.example.demo.dtos.rental.request.MaintenanceLogRequestDto;
import com.example.demo.dtos.rental.response.MaintenanceLogResponseDto;

import java.util.List;

public interface MaintenanceLogService {
    MaintenanceLogResponseDto createMaintenanceLog(MaintenanceLogRequestDto requestDto);
    MaintenanceLogResponseDto getMaintenanceLogById(Long id);
    List<MaintenanceLogResponseDto> getLogsByEquipmentId(Long equipmentId);
    List<MaintenanceLogResponseDto> getAllMaintenanceLogs();
    void deleteMaintenanceLog(Long id);

    MaintenanceLogResponseDto completeMaintenance(Long id, MaintenanceCompleteRequestDto requestDto);
}