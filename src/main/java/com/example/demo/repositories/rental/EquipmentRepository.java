package com.example.demo.repositories.rental;

import com.example.demo.enums.EquipmentCondition;
import com.example.demo.enums.EquipmentType;
import com.example.demo.models.rental.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    List<Equipment> findByType(EquipmentType type);

    List<Equipment> findByCondition(EquipmentCondition condition);
}