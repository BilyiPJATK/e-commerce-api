package com.example.demo.mappers;

import com.example.demo.dtos.request.UserRequestDto;
import com.example.demo.dtos.response.UserResponseDto;
import com.example.demo.models.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRequestDto dto);
    UserResponseDto toResponseDto(User entity);
}
