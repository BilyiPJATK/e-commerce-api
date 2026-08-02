package com.example.demo.mappers;

import com.example.demo.dtos.users.request.UserRequestDto;
import com.example.demo.dtos.users.response.UserResponseDto;
import com.example.demo.models.users.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRequestDto dto);
    UserResponseDto toResponseDto(User entity);
}
