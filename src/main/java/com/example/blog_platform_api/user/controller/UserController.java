package com.example.blog_platform_api.user.controller;

import com.example.blog_platform_api.user.dto.response.UserResponseDto;
import com.example.blog_platform_api.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
@Tag(name = "User Management API", description = "APIs for managing users in the system")
public class UserController {


    private final UserService service;


    //Get all Users
    @Operation(summary = "Get all users", description = "Retrieve a list of all registered users")
/*
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved users"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
*/

    @GetMapping
    public List<UserResponseDto> getAllUser() {
        return service.getAllUser();
    }




    //Get Users by id
    @Operation(summary = "Get user by ID", description = "Retrieve a specific user by their unique ID")
/*    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })*/
    @GetMapping("/{id}")
    public UserResponseDto getUserById(
            @Parameter(description = "ID of the user to be retrieved", example = "1")
            @PathVariable Long id
    ) {
        return service.getUserById(id);
    }




    //Block Users
    @Operation(summary = "Block user", description = "Block a user by their ID")
/*    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User block successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })*/
    @PatchMapping("/{id}/block")
    public ResponseEntity<?> blockUser(@PathVariable Long id) {
        service.blockUser(id);
        return ResponseEntity.ok("User blocked successfully");
    }



    //Unblock Users
    @Operation(summary = "Unblock user", description = "Unblock a user by their ID")
/*
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User Unblock  successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
*/

    @PatchMapping("/{id}/unblock")
    public ResponseEntity<?> unblockUser(@PathVariable Long id) {
        service.unblockUser(id);
        return ResponseEntity.ok("User unblocked successfully");
    }
}