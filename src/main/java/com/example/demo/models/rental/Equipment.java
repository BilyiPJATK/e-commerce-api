package com.example.demo.models.rental;


import com.example.demo.enums.EquipmentCondition;
import com.example.demo.enums.EquipmentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "equipment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Equipment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;
    private String model;
    private String size;
    private String sku;
    private LocalDate purchaseDate;

    @Enumerated(EnumType.STRING)
    private EquipmentType type;

    @Enumerated(EnumType.STRING)
    private EquipmentCondition condition;

    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL)
    private List<MaintenanceLog> maintenanceLogs;

    @OneToMany(mappedBy = "equipment")
    private List<RentalTransaction> rentalHistory;
}
