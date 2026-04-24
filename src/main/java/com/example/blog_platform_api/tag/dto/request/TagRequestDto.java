package com.example.blog_platform_api.tag.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TagRequestDto(

        @NotBlank(message = "Tag name cannot be blank")
        @Size(min = 2, max = 30, message = "Tag name must be between 2 and 30 characters")
        String name
) {}
