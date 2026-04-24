package com.example.blog_platform_api.auth.mapper;

import com.example.blog_platform_api.auth.dto.request.RegisterRequest;
import com.example.blog_platform_api.auth.dto.response.RegisterResponse;
import com.example.blog_platform_api.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegisterMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "posts", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(RegisterRequest requestDto);

    RegisterResponse toDto(User user);
}

