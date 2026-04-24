package com.example.blog_platform_api.comment.controller;

import com.example.blog_platform_api.comment.dto.request.CommentRequestDto;
import com.example.blog_platform_api.comment.dto.response.CommentResponseDto;
import com.example.blog_platform_api.comment.service.CommentService;
import com.example.blog_platform_api.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@RequestMapping("/api/v1/posts/{postId}/comments")
@RequiredArgsConstructor
@Tag(name = "Comments", description = "Add, view, update, and delete comments on blog posts")
public class CommentController {

    private final CommentService commentService;



    @Operation(summary = "Add a comment", description = "Authenticated users can add a comment to a published post", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponseDto>> addComment(
            @Parameter(description = "Post ID") @PathVariable Long postId,
            @Valid @RequestBody CommentRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Comment added successfully", commentService.addComment(postId, dto)));
    }





    @Operation(summary = "Get comments for a post", description = "Returns a paginated list of comments for a post, newest first")
    @GetMapping
    public ResponseEntity<ApiResponse<Page<CommentResponseDto>>> getComments(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(ApiResponse.success(commentService.getCommentsByPost(postId, pageable)));
    }






    @Operation(summary = "Update a comment", description = "Comment owner can update their own comment", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponseDto>> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequestDto dto) {
        return ResponseEntity.ok(ApiResponse.success("Comment updated successfully",
                commentService.updateComment(commentId, dto)));
    }






    @Operation(summary = "Delete a comment", description = "Comment owner or admin can delete a comment", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok(ApiResponse.success("Comment deleted successfully", null));
    }



}
