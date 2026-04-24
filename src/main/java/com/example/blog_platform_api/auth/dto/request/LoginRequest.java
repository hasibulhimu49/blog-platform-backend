package com.example.blog_platform_api.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(

        @NotBlank(message = "Username cannot be blank")
        @Size(min = 3, max = 15, message = "Username must be between 3 and 15 characters")
        String username,

        @NotBlank(message = "Password cannot be blank")
        String password
) {}
