package com.example.demo.controllers.rental;

import com.example.demo.dtos.rental.request.RentalTransactionRequestDto;
import com.example.demo.dtos.rental.response.RentalTransactionResponseDto;
import com.example.demo.services.rental.RentalTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
public class RentalTransactionController {

    private final RentalTransactionService rentalService;

    @PostMapping("/checkout")
    public ResponseEntity<RentalTransactionResponseDto> checkoutEquipment(
            @RequestBody RentalTransactionRequestDto requestDto) {

        RentalTransactionResponseDto transaction = rentalService.rentEquipment(requestDto);
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    @PostMapping("/{transactionId}/return")
    public ResponseEntity<RentalTransactionResponseDto> returnEquipment(
            @PathVariable Long transactionId) {

        RentalTransactionResponseDto transaction = rentalService.returnEquipment(transactionId);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping
    public ResponseEntity<List<RentalTransactionResponseDto>> getAllRentals(
            @RequestParam(required = false) String status) {

        return ResponseEntity.ok(rentalService.getAllRentals(status));
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<RentalTransactionResponseDto>> getRentalsByMemberId(
            @PathVariable Long memberId) {

        return ResponseEntity.ok(rentalService.getRentalsByMemberId(memberId));
    }
}