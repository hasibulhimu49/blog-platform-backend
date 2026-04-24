package com.example.blog_platform_api.user.dto.response;

import com.example.blog_platform_api.common.enums.Role;
import com.example.blog_platform_api.common.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserResponseDto {

    private Long id;
    private String name;
    private String username;
    private String email;
    private Role role;
    private Status status;
    private LocalDateTime createdAt;
}
