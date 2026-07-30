package com.example.demo.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequestDto {

    @NotBlank(message = "username has to be non empty")
    @Size(min = 3, max = 50, message = "username has to be between 3 and 50 symbols")
    private String username;

    @NotBlank(message = "email is required")
    @Email(message = "incorrect email format")
    private String email;
}
