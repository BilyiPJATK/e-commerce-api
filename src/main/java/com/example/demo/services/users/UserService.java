package com.example.demo.services.users;

import com.example.demo.dtos.users.request.UserRequestDto;
import com.example.demo.dtos.users.request.UserUpdateDisplayNameRequestDto;
import com.example.demo.dtos.users.response.UserResponseDto;
import org.springframework.data.domain.Page;

public interface UserService {
    UserResponseDto createUser(UserRequestDto requestDto);
    Page<UserResponseDto> getAllUsers(int page,int size);
    UserResponseDto getUserById(Long id);
    UserResponseDto updateDisplayName(Long id, UserUpdateDisplayNameRequestDto requestDto);
}
