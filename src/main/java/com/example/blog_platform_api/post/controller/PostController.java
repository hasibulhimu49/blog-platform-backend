package com.example.blog_platform_api.post.controller;

import com.example.blog_platform_api.common.response.ApiResponse;
import com.example.blog_platform_api.post.dto.request.PostCreateRequestDto;
import com.example.blog_platform_api.post.dto.request.PostUpdateRequestDto;
import com.example.blog_platform_api.post.dto.response.PostResponseDto;
import com.example.blog_platform_api.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Tag(name = "Posts", description = "Create, read, update, delete and search blog posts")
public class PostController {

    private final PostService postService;



    @Operation(summary = "Create a post", description = "Authenticated users can create a new blog post", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    public ResponseEntity<ApiResponse<PostResponseDto>> createPost(
            @Valid @RequestBody PostCreateRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Post created successfully", postService.createPost(dto)));
    }





    @Operation(summary = "Get post by ID", description = "Retrieve a single published post by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponseDto>> getPostById(
            @Parameter(description = "Post ID") @PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(postService.getPostById(id)));
    }





    @Operation(summary = "Get all published posts", description = "Returns a paginated list of all published posts, newest first")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<PostResponseDto>>> getAllPosts(
            @Parameter(description = "Page number (0-indexed)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of posts per page") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(ApiResponse.success(postService.getAllPosts(pageable)));
    }





    @Operation(summary = "Get my posts", description = "Returns all posts by the currently authenticated user",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/my")
    public ResponseEntity<ApiResponse<Page<PostResponseDto>>> getMyPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(ApiResponse.success(postService.getMyPosts(pageable)));
    }





    @Operation(summary = "Get posts by user", description = "Returns paginated published posts by a specific user ID")
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<Page<PostResponseDto>>> getPostsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(ApiResponse.success(postService.getPostsByUser(userId, pageable)));
    }





    @Operation(summary = "Search posts", description = "Full-text search across post titles and content")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<PostResponseDto>>> searchPosts(
            @Parameter(description = "Search keyword") @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(ApiResponse.success(postService.searchPosts(keyword, pageable)));
    }





    @Operation(summary = "Update a post", description = "Update title, content, status, or tags (owner only)",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PostResponseDto>> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody PostUpdateRequestDto dto) {
        return ResponseEntity.ok(ApiResponse.success("Post updated successfully", postService.updatePost(id, dto)));
    }





    @Operation(summary = "Delete a post", description = "Soft-deletes a post (owner or admin only)",
            security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok(ApiResponse.success("Post deleted successfully", null));
    }



}