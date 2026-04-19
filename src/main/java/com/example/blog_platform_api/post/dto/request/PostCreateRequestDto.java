package com.example.blog_platform_api.post.dto.request;

public record PostCreateRequestDto(
        String title,
        String content
) {}
