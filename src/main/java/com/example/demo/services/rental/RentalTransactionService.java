package com.example.demo.services.rental;

import com.example.demo.dtos.rental.request.RentalTransactionRequestDto;
import com.example.demo.dtos.rental.response.RentalTransactionResponseDto;

public interface RentalTransactionService {
    RentalTransactionResponseDto rentEquipment(RentalTransactionRequestDto requestDto);
    RentalTransactionResponseDto returnEquipment(Long transactionId);
}