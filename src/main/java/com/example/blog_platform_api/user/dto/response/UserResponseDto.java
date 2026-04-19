package com.example.blog_platform_api.user.dto.response;

import com.example.blog_platform_api.common.enums.Role;
import com.example.blog_platform_api.common.enums.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {

    private Long id;

    private String name;

    @NotNull(message = "Username Cannot be null")
    private String username;

    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Status status;

}
