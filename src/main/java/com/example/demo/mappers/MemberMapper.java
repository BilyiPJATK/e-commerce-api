package com.example.demo.mappers;

import com.example.demo.dtos.users.request.MemberRequestDto;
import com.example.demo.dtos.users.response.MemberResponseDto;
import com.example.demo.models.users.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    Member toEntity(MemberRequestDto dto);

    MemberResponseDto toResponseDto(Member entity);
}