package com.example.demo.dtos.users.request;

import com.example.demo.enums.MembershipType;
import lombok.Data;

@Data
public class MemberRequestDto {
    private MembershipType membershipType;
}