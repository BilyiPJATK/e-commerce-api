package com.example.demo.dtos.users.response;

import com.example.demo.enums.MembershipType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MemberResponseDto {
    private Long id;
    private MembershipType membershipType;
    private LocalDate joinDate;
    private UserResponseDto user;
}