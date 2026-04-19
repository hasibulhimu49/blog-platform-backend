package com.example.blog_platform_api.post.dto.response;

public record PostResponseDto(
        Long id,
        String title,
        String content
) {}
