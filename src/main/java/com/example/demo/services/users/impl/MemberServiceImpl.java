package com.example.demo.services.users.impl;

import com.example.demo.dtos.users.request.MemberRequestDto;
import com.example.demo.dtos.users.response.MemberResponseDto;
import com.example.demo.enums.MembershipType;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.mappers.MemberMapper;
import com.example.demo.models.users.Member;
import com.example.demo.models.users.User;
import com.example.demo.repositories.users.MemberRepository;
import com.example.demo.repositories.users.UserRepository;
import com.example.demo.services.users.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final MemberMapper memberMapper;


    @Override
    @Transactional
    public MemberResponseDto createMemberFromEmail(String email, MemberRequestDto requestDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        if (memberRepository.existsByUser(user)) {
            throw new IllegalArgumentException("User is already a member");
        }

        Member member = new Member();
        member.setUser(user);
        member.setJoinDate(LocalDate.now());
        member.setMembershipType(requestDto.getMembershipType());

        Member savedMember = memberRepository.save(member);
        return memberMapper.toResponseDto(savedMember);
    }

    @Override
    public MemberResponseDto getMemberById(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with ID: " + id));

        return memberMapper.toResponseDto(member);
    }

    @Override
    public List<MemberResponseDto> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(memberMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public MemberResponseDto updateMembershipType(Long id, MembershipType newType) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with ID: " + id));

        member.setMembershipType(newType);
        Member updatedMember = memberRepository.save(member);

        return memberMapper.toResponseDto(updatedMember);
    }
}