package com.example.demo.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequestDto {

    @NotBlank(message = "display name has to be non empty")
    @Size(min = 3, max = 50, message = "display name has to be between 3 and 50 symbols")
    private String displayName;

    @NotBlank(message = "email is required")
    @Email(message = "incorrect email format")
    private String email;

    @NotBlank(message = "password is required")
    @Size(min = 8, message = "password must be at least 8 characters long")
    private String password;
}
