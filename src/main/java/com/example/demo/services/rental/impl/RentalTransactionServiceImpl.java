package com.example.demo.services.rental.impl;

import com.example.demo.dtos.rental.request.RentalTransactionRequestDto;
import com.example.demo.dtos.rental.response.RentalTransactionResponseDto;
import com.example.demo.exceptions.EquipmentUnavailableException;
import com.example.demo.exceptions.InvalidRentalActionException;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.mappers.RentalTransactionMapper;
import com.example.demo.models.rental.Equipment;
import com.example.demo.models.rental.RentalTransaction;
import com.example.demo.models.users.Member;
import com.example.demo.repositories.rental.EquipmentRepository;
import com.example.demo.repositories.rental.RentalTransactionRepository;
import com.example.demo.repositories.users.MemberRepository;
import com.example.demo.services.rental.RentalTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RentalTransactionServiceImpl implements RentalTransactionService {
    private final RentalTransactionRepository rentalRepository;
    private final EquipmentRepository equipmentRepository;
    private final MemberRepository memberRepository;
    private final RentalTransactionMapper rentalTransactionMapper;

    @Override
    @Transactional
    public RentalTransactionResponseDto rentEquipment(RentalTransactionRequestDto requestDto) {
        Member member = memberRepository.findById(requestDto.getMemberId())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with ID: " + requestDto.getMemberId()));

        Equipment equipment = equipmentRepository.findById(requestDto.getEquipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Equipment not found with ID: " + requestDto.getEquipmentId()));

        List<RentalTransaction> activeRentals = rentalRepository.findByActualReturnTimeIsNull();
        boolean isAlreadyRented = activeRentals.stream()
                .anyMatch(rental -> rental.getEquipment().getId().equals(requestDto.getEquipmentId()));

        if (isAlreadyRented) {
            throw new EquipmentUnavailableException("This equipment is currently rented out and unavailable.");
        }

        RentalTransaction transaction = new RentalTransaction();
        transaction.setMember(member);
        transaction.setEquipment(equipment);
        transaction.setCheckOutTime(LocalDateTime.now());
        transaction.setExpectedReturnTime(LocalDateTime.now().plusHours(requestDto.getRentalHours()));

        RentalTransaction savedTransaction = rentalRepository.save(transaction);
        return rentalTransactionMapper.toResponseDto(savedTransaction);
    }

    @Override
    @Transactional
    public RentalTransactionResponseDto returnEquipment(Long transactionId) {
        RentalTransaction transaction = rentalRepository.findById(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with ID: " + transactionId));

        if (transaction.getActualReturnTime() != null) {
            throw new InvalidRentalActionException("This transaction has already been closed.");
        }

        transaction.setActualReturnTime(LocalDateTime.now());
        RentalTransaction updatedTransaction = rentalRepository.save(transaction);
        return rentalTransactionMapper.toResponseDto(updatedTransaction);
    }
}