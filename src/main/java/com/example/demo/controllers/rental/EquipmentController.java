package com.example.demo.controllers.rental;

import com.example.demo.dtos.rental.request.EquipmentRequestDto;
import com.example.demo.dtos.rental.response.EquipmentResponseDto;
import com.example.demo.enums.EquipmentCondition;
import com.example.demo.enums.EquipmentType;
import com.example.demo.services.rental.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment")
@RequiredArgsConstructor
public class EquipmentController {

    private final EquipmentService equipmentService;

    @PostMapping
    public ResponseEntity<EquipmentResponseDto> addEquipment(@RequestBody EquipmentRequestDto requestDto) {
        EquipmentResponseDto createdEquipment = equipmentService.addEquipment(requestDto);
        return new ResponseEntity<>(createdEquipment, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipmentResponseDto> getEquipmentById(@PathVariable Long id) {
        EquipmentResponseDto equipment = equipmentService.getEquipmentById(id);
        return ResponseEntity.ok(equipment);
    }

    @GetMapping
    public ResponseEntity<List<EquipmentResponseDto>> getAllEquipment(
            @RequestParam(required = false) EquipmentType type,
            @RequestParam(required = false) EquipmentCondition condition) {

        List<EquipmentResponseDto> equipmentList;

        if (type != null) {
            equipmentList = equipmentService.getEquipmentByType(type);
        } else if (condition != null) {
            equipmentList = equipmentService.getEquipmentByCondition(condition);
        } else {
            equipmentList = equipmentService.getAllEquipment();
        }

        return ResponseEntity.ok(equipmentList);
    }

    @PatchMapping("/{id}/condition")
    public ResponseEntity<EquipmentResponseDto> updateEquipmentCondition(
            @PathVariable Long id,
            @RequestParam EquipmentCondition newCondition) {
        EquipmentResponseDto updatedEquipment = equipmentService.updateEquipmentCondition(id, newCondition);
        return ResponseEntity.ok(updatedEquipment);
    }

    @PatchMapping("/{id}/retire")
    public ResponseEntity<Void> retireEquipment(@PathVariable Long id) {
        equipmentService.retireEquipment(id);
        return ResponseEntity.noContent().build();
    }
}