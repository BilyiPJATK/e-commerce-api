package com.example.demo.dtos.rental.response;

import com.example.demo.dtos.rental.response.EquipmentResponseDto;
import com.example.demo.dtos.users.response.MemberResponseDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RentalTransactionResponseDto {
    private Long id;
    private MemberResponseDto member;
    private EquipmentResponseDto equipment;
    private LocalDateTime checkOutTime;
    private LocalDateTime expectedReturnTime;
    private LocalDateTime actualReturnTime;
}