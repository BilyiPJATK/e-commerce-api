package com.example.demo.controllers.users;


import com.example.demo.dtos.users.request.UserRequestDto;
import com.example.demo.dtos.users.request.UserUpdateDisplayNameRequestDto;
import com.example.demo.dtos.users.response.UserResponseDto;
import com.example.demo.services.users.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        UserResponseDto createdUser = userService.createUser(userRequestDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        if (size > 100) {
            size = 100;
        }
        return ResponseEntity.ok(userService.getAllUsers(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PatchMapping("/{id}/display-name")
    public ResponseEntity<UserResponseDto> updateDisplayName(
            @PathVariable Long id,
            @RequestBody UserUpdateDisplayNameRequestDto requestDto) {
        UserResponseDto updatedUser = userService.updateDisplayName(id, requestDto);
        return ResponseEntity.ok(updatedUser);
    }
}
