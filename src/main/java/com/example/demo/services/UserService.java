package com.example.demo.services;

import com.example.demo.dtos.request.UserRequestDto;
import com.example.demo.dtos.response.UserResponseDto;
import com.example.demo.models.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    UserResponseDto createUser(UserRequestDto requestDto);
    Page<UserResponseDto> getAllUsers(int page,int size);
    UserResponseDto getUserById(Long id);
}
