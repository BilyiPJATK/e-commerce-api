package com.example.demo.services.users;

import com.example.demo.dtos.users.request.MemberRequestDto;
import com.example.demo.dtos.users.response.MemberResponseDto;
import com.example.demo.enums.MembershipType;

import java.util.List;

public interface MemberService {
    MemberResponseDto createMemberFromEmail(String email, MemberRequestDto requestDto);
    MemberResponseDto getMemberById(Long id);
    List<MemberResponseDto> getAllMembers();
    MemberResponseDto updateMembershipType(Long id, MembershipType newType);
}
