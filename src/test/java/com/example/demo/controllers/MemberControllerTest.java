package com.example.demo.controllers;

import com.example.demo.controllers.users.MemberController;
import com.example.demo.dtos.users.request.MemberRequestDto;
import com.example.demo.dtos.users.response.MemberResponseDto;
import com.example.demo.dtos.users.response.UserResponseDto;
import com.example.demo.enums.MembershipType;
import com.example.demo.security.JwtService;
import com.example.demo.services.users.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

@WebMvcTest(MemberController.class)
@AutoConfigureMockMvc(addFilters = false)
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Manually instantiated because Spring Boot 4.1 @WebMvcTest does not autowire ObjectMapper by default
    private ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @MockitoBean
    private MemberService memberService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    void createMember_ValidRequest_Returns201Created() throws Exception {
        MemberRequestDto request = new MemberRequestDto();
        request.setMembershipType(MembershipType.MONTHLY);

        UserResponseDto userDto = new UserResponseDto();
        userDto.setId(1L);
        userDto.setDisplayName("John Doe");
        userDto.setEmail("john@test.com");

        MemberResponseDto response = new MemberResponseDto();
        response.setId(1L);
        response.setMembershipType(MembershipType.MONTHLY);
        response.setJoinDate(LocalDate.now());
        response.setUser(userDto);

        when(memberService.createMemberFromEmail(eq("john@test.com"), any(MemberRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/members")
                        .principal(() -> "john@test.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.membershipType").value("MONTHLY"))
                .andExpect(jsonPath("$.user.email").value("john@test.com"));
    }

    @Test
    void getMemberById_ValidId_Returns200Ok() throws Exception {
        UserResponseDto userDto = new UserResponseDto();
        userDto.setId(1L);
        userDto.setDisplayName("John Doe");
        userDto.setEmail("john@test.com");

        MemberResponseDto response = new MemberResponseDto();
        response.setId(1L);
        response.setMembershipType(MembershipType.MONTHLY);
        response.setJoinDate(LocalDate.now());
        response.setUser(userDto);

        when(memberService.getMemberById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/members/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.membershipType").value("MONTHLY"))
                .andExpect(jsonPath("$.user.email").value("john@test.com"));
    }

    @Test
    void getAllMembers_Returns200Ok() throws Exception {
        UserResponseDto userDto = new UserResponseDto();
        userDto.setId(1L);
        userDto.setDisplayName("John Doe");
        userDto.setEmail("john@test.com");

        MemberResponseDto response = new MemberResponseDto();
        response.setId(1L);
        response.setMembershipType(MembershipType.MONTHLY);
        response.setJoinDate(LocalDate.now());
        response.setUser(userDto);

        when(memberService.getAllMembers()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].membershipType").value("MONTHLY"));
    }

    @Test
    void updateMembershipType_ValidRequest_Returns200Ok() throws Exception {
        UserResponseDto userDto = new UserResponseDto();
        userDto.setId(1L);
        userDto.setDisplayName("John Doe");
        userDto.setEmail("john@test.com");

        MemberResponseDto response = new MemberResponseDto();
        response.setId(1L);
        response.setMembershipType(MembershipType.ANNUAL);
        response.setJoinDate(LocalDate.now());
        response.setUser(userDto);

        when(memberService.updateMembershipType(1L, MembershipType.ANNUAL)).thenReturn(response);

        mockMvc.perform(patch("/api/members/{id}/membership", 1L)
                        .param("newType", "ANNUAL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.membershipType").value("ANNUAL"));
    }
}