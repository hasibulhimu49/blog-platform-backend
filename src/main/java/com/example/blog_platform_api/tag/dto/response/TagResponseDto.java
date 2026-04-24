package com.example.blog_platform_api.tag.dto.response;

public record TagResponseDto(
        Long id,
        String name,
        int postCount
) {}
