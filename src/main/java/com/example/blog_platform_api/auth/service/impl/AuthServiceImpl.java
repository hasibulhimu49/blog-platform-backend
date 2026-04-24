package com.example.blog_platform_api.auth.service.impl;

import com.example.blog_platform_api.auth.dto.request.LoginRequest;
import com.example.blog_platform_api.auth.dto.request.RegisterRequest;
import com.example.blog_platform_api.auth.dto.response.AuthResponse;
import com.example.blog_platform_api.auth.dto.response.RegisterResponse;
import com.example.blog_platform_api.auth.mapper.RegisterMapper;
import com.example.blog_platform_api.auth.service.AuthService;
import com.example.blog_platform_api.common.enums.Role;
import com.example.blog_platform_api.common.enums.Status;
import com.example.blog_platform_api.common.exception.DuplicateResourceException;
import com.example.blog_platform_api.common.exception.ForbiddenException;
import com.example.blog_platform_api.common.exception.ResourceNotFoundException;
import com.example.blog_platform_api.common.exception.UnauthorizedException;
import com.example.blog_platform_api.security.JwtService;
import com.example.blog_platform_api.security.UserPrincipal;
import com.example.blog_platform_api.user.dto.response.UserResponseDto;
import com.example.blog_platform_api.user.entity.User;
import com.example.blog_platform_api.user.mapper.UserMapper;
import com.example.blog_platform_api.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RegisterMapper mapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new DuplicateResourceException("Username '" + request.getUsername() + "' is already taken");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Email '" + request.getEmail() + "' is already registered");
        }

        User user = mapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIVE);

        User saved = userRepository.save(user);
        return mapper.toDto(saved);
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.username())
                .orElseThrow(() -> new UnauthorizedException("Invalid username or password"));

        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new UnauthorizedException("Invalid username or password");
        }

        if (user.getStatus() == Status.BLOCKED) {
            throw new ForbiddenException("Your account has been blocked. Please contact support.");
        }

        String token = jwtService.generateToken(new UserPrincipal(user));
        return new AuthResponse("Login successful", token);
    }

    @Override
    public UserResponseDto getCurrentUser() {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        User user = userRepository.findById(principal.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user not found"));
        return userMapper.toDto(user);
    }

    @Override
    public void logout() {
        // JWT is stateless — client must discard the token.
        // To implement server-side invalidation, add a token blacklist here (e.g., Redis).
    }
}
