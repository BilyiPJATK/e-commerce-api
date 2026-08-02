package com.example.demo.dtos.users.response;

import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;
    private String displayName;
    private String email;
}
