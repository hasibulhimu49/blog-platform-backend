package com.example.blog_platform_api.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest{

    @NotNull(message = "Name cannot be null")
    private String name;

    @NotNull(message = "Username cannot be null")
    @Pattern(regexp = "^[a-zA-Z0-9]{3,15}$",
            message = "Username must be 3–15 characters and contain only letters and numbers")
    private String username;

    @NotNull(message = "Email cannot be null")
    @Email()
    private String email;

    @NotNull(message = "Password cannot be null")
    private String password;
}