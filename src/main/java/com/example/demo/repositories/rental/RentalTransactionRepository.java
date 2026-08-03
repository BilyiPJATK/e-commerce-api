package com.example.demo.repositories.rental;

import com.example.demo.models.rental.RentalTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalTransactionRepository extends JpaRepository<RentalTransaction, Long> {

    List<RentalTransaction> findByMemberId(Long memberId);

    List<RentalTransaction> findByEquipmentId(Long equipmentId);

    // Find rentals that haven't been returned yet
    List<RentalTransaction> findByActualReturnTimeIsNull();

    boolean existsByEquipmentIdAndActualReturnTimeIsNull(Long equipmentId);
}
