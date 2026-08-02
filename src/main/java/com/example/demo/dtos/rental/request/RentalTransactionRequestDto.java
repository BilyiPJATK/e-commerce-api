package com.example.demo.dtos.rental.request;

import lombok.Data;

@Data
public class RentalTransactionRequestDto {
    private Long memberId;
    private Long equipmentId;
    private int rentalHours;
}