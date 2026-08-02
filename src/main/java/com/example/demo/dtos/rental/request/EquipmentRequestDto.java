package com.example.demo.dtos.rental.request;

import com.example.demo.enums.EquipmentCondition;
import com.example.demo.enums.EquipmentType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EquipmentRequestDto {
    private String brand;
    private String model;
    private String size;
    private String sku;
    private LocalDate purchaseDate;
    private EquipmentType type;
    private EquipmentCondition condition;

}
