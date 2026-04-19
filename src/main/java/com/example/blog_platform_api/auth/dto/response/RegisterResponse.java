package com.example.blog_platform_api.auth.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterResponse {
    private String name;
    private String username;
    private String email;
}
