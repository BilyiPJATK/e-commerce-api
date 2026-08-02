package com.example.demo.dtos.users.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequestDto {
    @NotBlank(message = "email is required")
    @Email(message = "incorrect email format")
    private String email;

    @NotBlank(message = "password is required")
    private String password;
}
