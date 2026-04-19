package com.example.blog_platform_api.post.service.impl;

import com.example.blog_platform_api.common.enums.Role;
import com.example.blog_platform_api.post.dto.request.PostCreateRequestDto;
import com.example.blog_platform_api.post.dto.response.PostResponseDto;
import com.example.blog_platform_api.post.entity.Post;
import com.example.blog_platform_api.post.mapper.PostMapper;
import com.example.blog_platform_api.post.repository.PostRepository;
import com.example.blog_platform_api.post.service.PostService;
import com.example.blog_platform_api.security.UserPrincipal;
import com.example.blog_platform_api.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;


    private User getCurrentUser() {
        UserPrincipal principal =
                (UserPrincipal) SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal();

        return principal.getUser();
    }

    @Override
    public PostResponseDto createPost(PostCreateRequestDto dto) {

        User user = getCurrentUser(); //user from JWT

        Post post = postMapper.toEntity(dto, user);


        Post savedPost = postRepository.save(post);

        return postMapper.toDto(savedPost);
    }

    @Override
    public PostResponseDto getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        return postMapper.toDto(post);
    }

    @Override
    public List<PostResponseDto> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::toDto)
                .toList();
    }

    @Override
    public PostResponseDto updatePost(Long id, PostCreateRequestDto dto) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User user = getCurrentUser();

        // ownership check
        if (!post.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You cannot update others' post");
        }

        post.setTitle(dto.title());
        post.setContent(dto.content());

        return postMapper.toDto(postRepository.save(post));
    }

    @Override
    public void deletePost(Long id) {

        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User user = getCurrentUser();

        // owner or admin check
        if (!post.getUser().getId().equals(user.getId())
                && !user.getRole().name().equals("ADMIN")) {
            throw new RuntimeException("Not allowed to delete this post");
        }

        postRepository.delete(post);
    }
}