package com.example.demo.controllers;

import com.example.demo.controllers.rental.RentalTransactionController;
import com.example.demo.dtos.rental.request.RentalTransactionRequestDto;
import com.example.demo.dtos.rental.response.EquipmentResponseDto;
import com.example.demo.dtos.rental.response.RentalTransactionResponseDto;
import com.example.demo.dtos.users.response.MemberResponseDto;
import com.example.demo.security.JwtService;
import com.example.demo.services.rental.RentalTransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RentalTransactionController.class)
@AutoConfigureMockMvc(addFilters = false)
public class RentalTransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Manually instantiated because Spring Boot 4.1 @WebMvcTest does not autowire ObjectMapper by default
    private ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @MockitoBean
    private RentalTransactionService rentalService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    void checkoutEquipment_ValidRequest_Returns201Created() throws Exception {
        RentalTransactionRequestDto request = new RentalTransactionRequestDto();
         request.setMemberId(1L);
         request.setEquipmentId(1L);

        MemberResponseDto member = new MemberResponseDto();
        member.setId(1L);

        EquipmentResponseDto equipment = new EquipmentResponseDto();
        equipment.setId(1L);

        RentalTransactionResponseDto response = new RentalTransactionResponseDto();
        response.setId(1L);
        response.setMember(member);
        response.setEquipment(equipment);
        response.setCheckOutTime(LocalDateTime.now());
        response.setExpectedReturnTime(LocalDateTime.now().plusDays(1));

        when(rentalService.rentEquipment(any(RentalTransactionRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/rentals/checkout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.member.id").value(1))
                .andExpect(jsonPath("$.equipment.id").value(1))
                .andExpect(jsonPath("$.checkOutTime").exists())
                .andExpect(jsonPath("$.expectedReturnTime").exists());
    }

    @Test
    void returnEquipment_ValidTransactionId_Returns200Ok() throws Exception {
        MemberResponseDto member = new MemberResponseDto();
        member.setId(1L);

        EquipmentResponseDto equipment = new EquipmentResponseDto();
        equipment.setId(1L);

        RentalTransactionResponseDto response = new RentalTransactionResponseDto();
        response.setId(1L);
        response.setMember(member);
        response.setEquipment(equipment);
        response.setCheckOutTime(LocalDateTime.now().minusDays(1));
        response.setExpectedReturnTime(LocalDateTime.now());
        response.setActualReturnTime(LocalDateTime.now());

        when(rentalService.returnEquipment(1L)).thenReturn(response);

        mockMvc.perform(post("/api/rentals/{transactionId}/return", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.member.id").value(1))
                .andExpect(jsonPath("$.equipment.id").value(1))
                .andExpect(jsonPath("$.actualReturnTime").exists());
    }
}