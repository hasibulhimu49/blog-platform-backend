package com.example.blog_platform_api.comment.dto.response;

import java.time.LocalDateTime;

public record CommentResponseDto(
        Long id,
        String content,
        Long authorId,
        String authorUsername,
        Long postId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
