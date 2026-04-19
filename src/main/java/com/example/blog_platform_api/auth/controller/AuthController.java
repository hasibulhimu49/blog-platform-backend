package com.example.blog_platform_api.auth.controller;

import com.example.blog_platform_api.auth.dto.request.LoginRequest;
import com.example.blog_platform_api.auth.dto.request.RegisterRequest;
import com.example.blog_platform_api.auth.dto.response.AuthResponse;
import com.example.blog_platform_api.auth.dto.response.RegisterResponse;
import com.example.blog_platform_api.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
@Slf4j
@Tag(name = "Authentication API", description = "APIs for user authentication (login, register, logout)")
public class AuthController {


    private final AuthService service;


    //Registration
    @Operation(
            summary = "Register a new user",
            description = "Create a new user account using username and password"
    )
/*    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })*/
    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest registerRequest) {
        return service.register(registerRequest);
    }



    //Login
    @Operation(
            summary = "User login",
            description = "Authenticate user and return JWT token"
    )
/*    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid username or password"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })*/
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(service.login(loginRequest));
    }



    //Logout
    @Operation(
            summary = "User logout",
            description = "Logout the currently authenticated user"
    )
 /*   @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logout successful"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })*/
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        service.logout();
        return ResponseEntity.ok("Logout Successfully");
    }
}