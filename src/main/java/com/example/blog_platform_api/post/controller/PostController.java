package com.example.blog_platform_api.post.controller;

import com.example.blog_platform_api.post.dto.request.PostCreateRequestDto;
import com.example.blog_platform_api.post.dto.response.PostResponseDto;
import com.example.blog_platform_api.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // Create Post
    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(
            @RequestBody PostCreateRequestDto dto
    ) {
        return ResponseEntity.ok(postService.createPost(dto));
    }

    //  Get Post by ID
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPostById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    //  Get All Posts
    @GetMapping
    public ResponseEntity<List<PostResponseDto>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    //  Update Post
    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDto> updatePost(
            @PathVariable Long id,
            @RequestBody PostCreateRequestDto dto
    ) {
        return ResponseEntity.ok(postService.updatePost(id, dto));
    }

    //  Delete Post
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(
            @PathVariable Long id
    ) {
        postService.deletePost(id);
        return ResponseEntity.ok("Post deleted successfully");
    }
}