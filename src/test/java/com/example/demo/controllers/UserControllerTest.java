package com.example.demo.controllers;

import com.example.demo.controllers.users.UserController;
import com.example.demo.dtos.users.request.UserRequestDto;
import com.example.demo.dtos.users.request.UserUpdateDisplayNameRequestDto;
import com.example.demo.dtos.users.response.UserResponseDto;
import com.example.demo.security.JwtService;
import com.example.demo.services.users.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Manually instantiated because Spring Boot 4.1 @WebMvcTest does not autowire ObjectMapper by default
    private ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    void createUser_ValidRequest_Returns201Created() throws Exception {
        UserRequestDto request = new UserRequestDto();
        request.setDisplayName("John Doe");
        request.setEmail("doe@email.com");
        request.setPassword("password123");

        UserResponseDto response = new UserResponseDto();
        response.setId(1L);
        response.setDisplayName("John Doe");
        response.setEmail("doe@email.com");

        when(userService.createUser(any(UserRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.displayName").value("John Doe"))
                .andExpect(jsonPath("$.email").value("doe@email.com"));
    }

    @Test
    void getAllUsers_Returns200Ok() throws Exception {
        UserResponseDto response = new UserResponseDto();
        response.setId(1L);
        response.setDisplayName("John Doe");
        response.setEmail("doe@email.com");

        when(userService.getAllUsers(0, 10)).thenReturn(new PageImpl<>(List.of(response)));

        mockMvc.perform(get("/api/users")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].displayName").value("John Doe"))
                .andExpect(jsonPath("$.content[0].email").value("doe@email.com"));
    }

    @Test
    void getUserById_ValidId_Returns200Ok() throws Exception {
        UserResponseDto response = new UserResponseDto();
        response.setId(1L);
        response.setDisplayName("John Doe");
        response.setEmail("doe@email.com");

        when(userService.getUserById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/users/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.displayName").value("John Doe"))
                .andExpect(jsonPath("$.email").value("doe@email.com"));
    }

    @Test
    void updateDisplayName_ValidRequest_Returns200Ok() throws Exception {
        UserUpdateDisplayNameRequestDto request = new UserUpdateDisplayNameRequestDto();
        request.setDisplayName("Jane Doe");

        UserResponseDto response = new UserResponseDto();
        response.setId(1L);
        response.setDisplayName("Jane Doe");
        response.setEmail("doe@email.com");

        when(userService.updateDisplayName(eq(1L), any(UserUpdateDisplayNameRequestDto.class))).thenReturn(response);

        mockMvc.perform(patch("/api/users/{id}/display-name", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.displayName").value("Jane Doe"))
                .andExpect(jsonPath("$.email").value("doe@email.com"));
    }
}