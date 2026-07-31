package com.example.demo.dtos.response;

import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;
    private String displayName;
    private String email;
}
