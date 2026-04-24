package com.example.blog_platform_api.comment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentRequestDto(

        @NotBlank(message = "Comment content cannot be blank")
        @Size(min = 1, max = 1000, message = "Comment must be between 1 and 1000 characters")
        String content
) {}
