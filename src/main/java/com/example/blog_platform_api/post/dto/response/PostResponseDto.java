package com.example.blog_platform_api.post.dto.response;

import com.example.blog_platform_api.common.enums.PostStatus;

import java.time.LocalDateTime;
import java.util.Set;

public record PostResponseDto(
        Long id,
        String title,
        String content,
        PostStatus status,
        Long authorId,
        String authorUsername,
        Set<String> tags,
        long commentCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
