package com.example.blog_platform_api.auth.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public record LoginRequest (
        @NotNull(message = "can not be null")
        @Size(min = 3,max = 15,message = "3 to 15")
        String username,

        @NotNull(message = "Password can not be null")
        String password


)
{ }
