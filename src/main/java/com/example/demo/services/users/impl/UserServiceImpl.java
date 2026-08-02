package com.example.demo.services.users.impl;

import com.example.demo.dtos.users.request.UserRequestDto;
import com.example.demo.dtos.users.request.UserUpdateDisplayNameRequestDto;
import com.example.demo.dtos.users.response.UserResponseDto;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.mappers.UserMapper;
import com.example.demo.models.users.User;
import com.example.demo.repositories.users.UserRepository;
import com.example.demo.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto createUser(UserRequestDto requestDto) {
        Optional<User> existingUser = userRepository.findByEmail(requestDto.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("A user with this email already exists");
        }

        User user = userMapper.toEntity(requestDto);

        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));

        User savedUser = userRepository.save(user);
        return userMapper.toResponseDto(savedUser);
    }

    @Override
    public Page<UserResponseDto> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> productPage = userRepository.findAll(pageable);

        return productPage.map(userMapper::toResponseDto);
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return userMapper.toResponseDto(user);
    }

    @Override
    @Transactional
    public UserResponseDto updateDisplayName(Long id, UserUpdateDisplayNameRequestDto requestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        user.setDisplayName(requestDto.getDisplayName());
        User updatedUser = userRepository.save(user);
        return userMapper.toResponseDto(updatedUser);
    }
}
