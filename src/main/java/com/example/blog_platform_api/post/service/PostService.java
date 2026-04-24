package com.example.blog_platform_api.post.service;

import com.example.blog_platform_api.post.dto.request.PostCreateRequestDto;
import com.example.blog_platform_api.post.dto.request.PostUpdateRequestDto;
import com.example.blog_platform_api.post.dto.response.PostResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {

    PostResponseDto createPost(PostCreateRequestDto dto);

    PostResponseDto getPostById(Long id);

    Page<PostResponseDto> getAllPosts(Pageable pageable);

    Page<PostResponseDto> getMyPosts(Pageable pageable);

    Page<PostResponseDto> getPostsByUser(Long userId, Pageable pageable);

    Page<PostResponseDto> searchPosts(String keyword, Pageable pageable);

    PostResponseDto updatePost(Long id, PostUpdateRequestDto dto);

    void deletePost(Long id);
}