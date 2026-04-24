package com.example.blog_platform_api.user.controller;

import com.example.blog_platform_api.common.response.ApiResponse;
import com.example.blog_platform_api.user.dto.response.UserResponseDto;
import com.example.blog_platform_api.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Admin-only: manage and moderate users")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService service;



    @Operation(summary = "Get all users", description = "Admin only — returns a list of all registered users")
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getAllUsers() {
        return ResponseEntity.ok(ApiResponse.success(service.getAllUser()));
    }



    @Operation(summary = "Get user by ID", description = "Admin only — retrieve a specific user by their ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserById(
            @Parameter(description = "User ID") @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(service.getUserById(id)));
    }



    @Operation(summary = "Block user", description = "Admin only — blocks a user, preventing them from logging in")
    @PatchMapping("/{id}/block")
    public ResponseEntity<ApiResponse<Void>> blockUser(@PathVariable Long id) {
        service.blockUser(id);
        return ResponseEntity.ok(ApiResponse.success("User blocked successfully", null));
    }



    @Operation(summary = "Unblock user", description = "Admin only — restores a blocked users access")
    @PatchMapping("/{id}/unblock")
    public ResponseEntity<ApiResponse<Void>> unblockUser(@PathVariable Long id) {
        service.unblockUser(id);
        return ResponseEntity.ok(ApiResponse.success("User unblocked successfully", null));
    }
}