package com.example.demo.services;

import com.example.demo.dtos.users.request.UserRequestDto;
import com.example.demo.dtos.users.response.UserResponseDto;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.mappers.UserMapper;
import com.example.demo.models.users.User;
import com.example.demo.repositories.users.UserRepository;
import com.example.demo.services.users.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void getUserById_Success_ReturnsUserDto() {
        Long userId = 1L;
        User fakeUser = new User();
        fakeUser.setId(userId);
        fakeUser.setEmail("test@example.com");

        UserResponseDto fakeDto = new UserResponseDto();
        fakeDto.setEmail("test@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(fakeUser));
        when(userMapper.toResponseDto(fakeUser)).thenReturn(fakeDto);

        UserResponseDto result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void getUserById_UserNotFound_ThrowsException() {
        Long userId = 99L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(userId);
        });
        verify(userMapper, never()).toResponseDto(any());
    }

    @Test
    void addUser_Success_ReturnsSavedUserDto() {
        UserRequestDto requestDto = new UserRequestDto();
        requestDto.setEmail("new@example.com");
        requestDto.setPassword("plainTextPassword");

        User mappedEntity = new User();
        mappedEntity.setEmail("new@example.com");

        User savedEntity = new User();
        savedEntity.setId(2L);
        savedEntity.setEmail("new@example.com");

        UserResponseDto expectedResponse = new UserResponseDto();
        expectedResponse.setId(2L);
        expectedResponse.setEmail("new@example.com");

        when(userMapper.toEntity(requestDto)).thenReturn(mappedEntity);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(mappedEntity)).thenReturn(savedEntity);
        when(userMapper.toResponseDto(savedEntity)).thenReturn(expectedResponse);

        UserResponseDto result = userService.createUser(requestDto);

        assertNotNull(result);
        assertEquals(2L, result.getId());
        verify(userRepository, times(1)).save(mappedEntity);
        verify(passwordEncoder, times(1)).encode("plainTextPassword");
    }

    @Test
    void getAllUsers_ReturnsPaginatedUsers() {
        Pageable pageable = PageRequest.of(0, 10);
        User user = new User();
        user.setId(1L);

        Page<User> userPage = new PageImpl<>(List.of(user));
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(1L);

        when(userRepository.findAll(pageable)).thenReturn(userPage);
        when(userMapper.toResponseDto(user)).thenReturn(responseDto);

        Page<UserResponseDto> result = userService.getAllUsers(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(userRepository, times(1)).findAll(pageable);
    }

    @Test
    void addUser_EmailAlreadyExists_ThrowsException() {
        UserRequestDto requestDto = new UserRequestDto();
        requestDto.setEmail("duplicate@example.com");

        User existingUser = new User();
        existingUser.setEmail("duplicate@example.com");

        when(userRepository.findByEmail(requestDto.getEmail())).thenReturn(Optional.of(existingUser));

        assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(requestDto);
        });

        verify(userRepository, never()).save(any());
    }
}
