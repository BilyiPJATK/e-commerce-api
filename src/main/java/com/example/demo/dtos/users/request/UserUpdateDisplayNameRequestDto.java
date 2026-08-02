package com.example.demo.dtos.users.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserUpdateDisplayNameRequestDto {
    @NotBlank(message = "Display name cannot be blank")
    private String displayName;
}