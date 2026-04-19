package com.example.blog_platform_api.post.service;

import com.example.blog_platform_api.post.dto.request.PostCreateRequestDto;
import com.example.blog_platform_api.post.dto.response.PostResponseDto;

import java.util.List;

public interface PostService {

    PostResponseDto createPost(PostCreateRequestDto dto);

    PostResponseDto getPostById(Long id);

    List<PostResponseDto> getAllPosts();

    PostResponseDto updatePost(Long id, PostCreateRequestDto dto);

    void deletePost(Long id);
}