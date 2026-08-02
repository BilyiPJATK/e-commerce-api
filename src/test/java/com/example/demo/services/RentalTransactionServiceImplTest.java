package com.example.demo.services;

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
import com.example.demo.services.rental.impl.RentalTransactionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentalTransactionServiceImplTest {

    @Mock
    private RentalTransactionRepository rentalRepository;

    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private RentalTransactionMapper rentalTransactionMapper;

    @InjectMocks
    private RentalTransactionServiceImpl rentalService;

    @Test
    void rentEquipment_Success_ReturnsSavedTransaction() {
        RentalTransactionRequestDto requestDto = new RentalTransactionRequestDto();
        requestDto.setMemberId(1L);
        requestDto.setEquipmentId(1L);
        requestDto.setRentalHours(24);

        Member member = new Member();
        member.setId(1L);

        Equipment equipment = new Equipment();
        equipment.setId(1L);

        RentalTransaction savedTransaction = new RentalTransaction();
        savedTransaction.setId(1L);

        RentalTransactionResponseDto responseDto = new RentalTransactionResponseDto();
        responseDto.setId(1L);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));

        when(rentalRepository.findByActualReturnTimeIsNull()).thenReturn(Collections.emptyList());
        when(rentalRepository.save(any(RentalTransaction.class))).thenReturn(savedTransaction);
        when(rentalTransactionMapper.toResponseDto(savedTransaction)).thenReturn(responseDto);

        RentalTransactionResponseDto result = rentalService.rentEquipment(requestDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(rentalRepository, times(1)).save(any(RentalTransaction.class));
    }

    @Test
    void rentEquipment_AlreadyRented_ThrowsException() {
        RentalTransactionRequestDto requestDto = new RentalTransactionRequestDto();
        requestDto.setMemberId(1L);
        requestDto.setEquipmentId(1L);

        Member member = new Member();
        Equipment equipment = new Equipment();
        equipment.setId(1L);

        RentalTransaction activeTransaction = new RentalTransaction();
        activeTransaction.setEquipment(equipment);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));
        when(rentalRepository.findByActualReturnTimeIsNull()).thenReturn(List.of(activeTransaction));

        assertThrows(EquipmentUnavailableException.class, () -> rentalService.rentEquipment(requestDto));
        verify(rentalRepository, never()).save(any());
    }

    @Test
    void returnEquipment_Success_SetsActualReturnTime() {
        RentalTransaction transaction = new RentalTransaction();
        transaction.setId(1L);
        transaction.setActualReturnTime(null); // Explicitly null to simulate active rental

        RentalTransactionResponseDto responseDto = new RentalTransactionResponseDto();
        responseDto.setId(1L);

        when(rentalRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(rentalRepository.save(transaction)).thenReturn(transaction);
        when(rentalTransactionMapper.toResponseDto(transaction)).thenReturn(responseDto);

        RentalTransactionResponseDto result = rentalService.returnEquipment(1L);

        assertNotNull(result);
        assertNotNull(transaction.getActualReturnTime());
        verify(rentalRepository, times(1)).save(transaction);
    }

    @Test
    void returnEquipment_AlreadyClosed_ThrowsException() {
        RentalTransaction transaction = new RentalTransaction();
        transaction.setId(1L);
        transaction.setActualReturnTime(LocalDateTime.now()); // Already returned

        when(rentalRepository.findById(1L)).thenReturn(Optional.of(transaction));

        assertThrows(InvalidRentalActionException.class, () -> rentalService.returnEquipment(1L));
        verify(rentalRepository, never()).save(any());
    }
}