package com.example.demo.services;

import com.example.demo.dtos.users.request.MemberRequestDto;
import com.example.demo.dtos.users.response.MemberResponseDto;
import com.example.demo.enums.MembershipType;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.mappers.MemberMapper;
import com.example.demo.models.users.Member;
import com.example.demo.models.users.User;
import com.example.demo.repositories.users.MemberRepository;
import com.example.demo.repositories.users.UserRepository;
import com.example.demo.services.users.impl.MemberServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MemberMapper memberMapper;

    @InjectMocks
    private MemberServiceImpl memberService;

    @Test
    void createMemberFromEmail_Success_ReturnsSavedMemberDto() {
        String email = "john@test.com";
        MemberRequestDto requestDto = new MemberRequestDto();
        requestDto.setMembershipType(MembershipType.MONTHLY);

        User user = new User();
        user.setId(1L);
        user.setEmail(email);

        Member savedMember = new Member();
        savedMember.setId(1L);
        savedMember.setUser(user);
        savedMember.setMembershipType(MembershipType.MONTHLY);
        savedMember.setJoinDate(LocalDate.now());

        MemberResponseDto expectedResponse = new MemberResponseDto();
        expectedResponse.setId(1L);
        expectedResponse.setMembershipType(MembershipType.MONTHLY);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(memberRepository.existsByUser(user)).thenReturn(false);
        when(memberRepository.save(any(Member.class))).thenReturn(savedMember);
        when(memberMapper.toResponseDto(savedMember)).thenReturn(expectedResponse);

        MemberResponseDto result = memberService.createMemberFromEmail(email, requestDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(MembershipType.MONTHLY, result.getMembershipType());
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void createMemberFromEmail_UserNotFound_ThrowsException() {
        String email = "unknown@test.com";
        MemberRequestDto requestDto = new MemberRequestDto();

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> memberService.createMemberFromEmail(email, requestDto));
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void createMemberFromEmail_UserAlreadyMember_ThrowsException() {
        String email = "john@test.com";
        MemberRequestDto requestDto = new MemberRequestDto();

        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(memberRepository.existsByUser(user)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> memberService.createMemberFromEmail(email, requestDto));
        verify(memberRepository, never()).save(any(Member.class));
    }

    @Test
    void getMemberById_Success_ReturnsDto() {
        Member member = new Member();
        member.setId(1L);

        MemberResponseDto responseDto = new MemberResponseDto();
        responseDto.setId(1L);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(memberMapper.toResponseDto(member)).thenReturn(responseDto);

        MemberResponseDto result = memberService.getMemberById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getMemberById_NotFound_ThrowsException() {
        when(memberRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> memberService.getMemberById(99L));
    }

    @Test
    void getAllMembers_ReturnsListOfMembers() {
        Member member = new Member();
        member.setId(1L);

        MemberResponseDto responseDto = new MemberResponseDto();
        responseDto.setId(1L);

        when(memberRepository.findAll()).thenReturn(List.of(member));
        when(memberMapper.toResponseDto(member)).thenReturn(responseDto);

        List<MemberResponseDto> result = memberService.getAllMembers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(memberRepository, times(1)).findAll();
    }

    @Test
    void updateMembershipType_Success_ReturnsUpdatedDto() {
        Member member = new Member();
        member.setId(1L);
        member.setMembershipType(MembershipType.MONTHLY);

        MemberResponseDto responseDto = new MemberResponseDto();
        responseDto.setId(1L);
        responseDto.setMembershipType(MembershipType.ANNUAL);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(memberRepository.save(member)).thenReturn(member);
        when(memberMapper.toResponseDto(member)).thenReturn(responseDto);

        MemberResponseDto result = memberService.updateMembershipType(1L, MembershipType.ANNUAL);

        assertNotNull(result);
        assertEquals(MembershipType.ANNUAL, member.getMembershipType());
        assertEquals(MembershipType.ANNUAL, result.getMembershipType());
        verify(memberRepository, times(1)).save(member);
    }

    @Test
    void updateMembershipType_NotFound_ThrowsException() {
        when(memberRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> memberService.updateMembershipType(99L, MembershipType.ANNUAL));
        verify(memberRepository, never()).save(any());
    }
}