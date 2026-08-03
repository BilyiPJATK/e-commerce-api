package com.example.demo.controllers.rental;

import com.example.demo.dtos.rental.request.MaintenanceCompleteRequestDto;
import com.example.demo.dtos.rental.request.MaintenanceLogRequestDto;
import com.example.demo.dtos.rental.response.MaintenanceLogResponseDto;
import com.example.demo.services.rental.MaintenanceLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/maintenance")
@RequiredArgsConstructor
public class MaintenanceLogController {

    private final MaintenanceLogService maintenanceLogService;

    @PostMapping
    public ResponseEntity<MaintenanceLogResponseDto> createMaintenanceLog(
            @RequestBody MaintenanceLogRequestDto requestDto) {
        MaintenanceLogResponseDto createdLog = maintenanceLogService.createMaintenanceLog(requestDto);
        return new ResponseEntity<>(createdLog, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceLogResponseDto> getMaintenanceLogById(@PathVariable Long id) {
        MaintenanceLogResponseDto log = maintenanceLogService.getMaintenanceLogById(id);
        return ResponseEntity.ok(log);
    }

    @GetMapping
    public ResponseEntity<List<MaintenanceLogResponseDto>> getAllOrFilteredMaintenanceLogs(
            @RequestParam(required = false) Long equipmentId) {

        List<MaintenanceLogResponseDto> logs;

        if (equipmentId != null) {
            logs = maintenanceLogService.getLogsByEquipmentId(equipmentId);
        } else {
            logs = maintenanceLogService.getAllMaintenanceLogs();
        }

        return ResponseEntity.ok(logs);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaintenanceLog(@PathVariable Long id) {
        maintenanceLogService.deleteMaintenanceLog(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<MaintenanceLogResponseDto> completeMaintenance(
            @PathVariable Long id,
            @RequestBody MaintenanceCompleteRequestDto requestDto) {

        MaintenanceLogResponseDto updatedLog = maintenanceLogService.completeMaintenance(id, requestDto);
        return ResponseEntity.ok(updatedLog);
    }
}