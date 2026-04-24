package com.example.blog_platform_api.tag.controller;

import com.example.blog_platform_api.common.response.ApiResponse;
import com.example.blog_platform_api.tag.dto.request.TagRequestDto;
import com.example.blog_platform_api.tag.dto.response.TagResponseDto;
import com.example.blog_platform_api.tag.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
@Tag(name = "Tags", description = "Manage blog post tags")
public class TagController {

    private final TagService tagService;

    @Operation(summary = "Get all tags", description = "Returns a list of all available tags")
    @GetMapping
    public ResponseEntity<ApiResponse<List<TagResponseDto>>> getAllTags() {
        return ResponseEntity.ok(ApiResponse.success(tagService.getAllTags()));
    }


    @Operation(summary = "Create a tag", description = "Admin only — creates a new tag", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    public ResponseEntity<ApiResponse<TagResponseDto>> createTag(@Valid @RequestBody TagRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tag created successfully", tagService.createTag(dto)));
    }


    @Operation(summary = "Delete a tag", description = "Admin only — deletes a tag by ID", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.ok(ApiResponse.success("Tag deleted successfully", null));
    }
}
