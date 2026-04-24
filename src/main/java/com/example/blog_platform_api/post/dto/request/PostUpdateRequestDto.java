package com.example.blog_platform_api.post.dto.request;

import com.example.blog_platform_api.common.enums.PostStatus;
import jakarta.validation.constraints.Size;

import java.util.List;

public record PostUpdateRequestDto(

        @Size(min = 3, max = 200, message = "Title must be between 3 and 200 characters")
        String title,

        @Size(min = 10, message = "Content must be at least 10 characters")
        String content,

        PostStatus status,

        List<String> tags
) {}
