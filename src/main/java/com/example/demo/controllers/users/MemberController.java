package com.example.demo.controllers.users;

import com.example.demo.dtos.users.request.MemberRequestDto;
import com.example.demo.dtos.users.response.MemberResponseDto;
import com.example.demo.enums.MembershipType;
import com.example.demo.services.users.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberResponseDto> createMember(
            Principal principal,
            @Valid @RequestBody MemberRequestDto requestDto) {

        String email = principal.getName();

        MemberResponseDto createdMember = memberService.createMemberFromEmail(email, requestDto);
        return new ResponseEntity<>(createdMember, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberResponseDto> getMemberById(@PathVariable Long id) {
        MemberResponseDto member = memberService.getMemberById(id);
        return ResponseEntity.ok(member);
    }

    @GetMapping
    public ResponseEntity<List<MemberResponseDto>> getAllMembers() {
        List<MemberResponseDto> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }

    @PatchMapping("/{id}/membership")
    public ResponseEntity<MemberResponseDto> updateMembershipType(
            @PathVariable Long id,
            @RequestParam MembershipType newType) {
        MemberResponseDto updatedMember = memberService.updateMembershipType(id, newType);
        return ResponseEntity.ok(updatedMember);
    }
}