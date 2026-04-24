package com.example.blog_platform_api.auth.controller;

import com.example.blog_platform_api.auth.dto.request.LoginRequest;
import com.example.blog_platform_api.auth.dto.request.RegisterRequest;
import com.example.blog_platform_api.auth.dto.response.AuthResponse;
import com.example.blog_platform_api.auth.dto.response.RegisterResponse;
import com.example.blog_platform_api.auth.service.AuthService;
import com.example.blog_platform_api.common.response.ApiResponse;
import com.example.blog_platform_api.user.dto.response.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Register, login, logout, and get current user profile")
public class AuthController {

    private final AuthService service;




    @Operation(summary = "Register a new user", description = "Create a new user account with name, username, email, and password")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "User registered successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation error"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Username or email already exists")
    })
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        RegisterResponse response = service.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User registered successfully", response));
    }







    @Operation(summary = "Login", description = "Authenticate with username and password to receive a JWT token")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Login successful"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "Account is blocked")
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(ApiResponse.success("Login successful", service.login(loginRequest)));
    }







    @Operation(summary = "Get current user profile", description = "Returns the profile of the currently authenticated user",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User profile returned"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Not authenticated")
    })
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponseDto>> getCurrentUser() {
        return ResponseEntity.ok(ApiResponse.success(service.getCurrentUser()));
    }







    @Operation(summary = "Logout", description = "Invalidates the current session — client must discard the JWT",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Logged out successfully")
    })
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        service.logout();
        return ResponseEntity.ok(ApiResponse.success("Logged out successfully", null));
    }




}