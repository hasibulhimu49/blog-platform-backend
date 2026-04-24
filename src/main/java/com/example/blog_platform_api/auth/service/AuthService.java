package com.example.blog_platform_api.auth.service;

import com.example.blog_platform_api.auth.dto.request.LoginRequest;
import com.example.blog_platform_api.auth.dto.request.RegisterRequest;
import com.example.blog_platform_api.auth.dto.response.AuthResponse;
import com.example.blog_platform_api.auth.dto.response.RegisterResponse;
import com.example.blog_platform_api.user.dto.response.UserResponseDto;

public interface AuthService {

    RegisterResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest loginRequest);

    UserResponseDto getCurrentUser();

    void logout();
}
