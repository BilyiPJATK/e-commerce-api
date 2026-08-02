package com.example.demo.controllers.users;


import com.example.demo.dtos.users.request.AuthRequestDto;
import com.example.demo.dtos.users.request.UserRequestDto;
import com.example.demo.dtos.users.response.AuthResponseDto;
import com.example.demo.dtos.users.response.UserResponseDto;
import com.example.demo.services.users.AuthService;
import com.example.demo.services.users.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@Valid @RequestBody UserRequestDto requestDto) {
        UserResponseDto response = userService.createUser(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody AuthRequestDto requestDto) {
        AuthResponseDto response = authService.login(requestDto);
        return ResponseEntity.ok(response);
    }
}
