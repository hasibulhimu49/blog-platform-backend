package com.example.blog_platform_api.post.service.impl;

import com.example.blog_platform_api.comment.repository.CommentRepository;
import com.example.blog_platform_api.common.enums.PostStatus;
import com.example.blog_platform_api.common.exception.ForbiddenException;
import com.example.blog_platform_api.common.exception.ResourceNotFoundException;
import com.example.blog_platform_api.post.dto.request.PostCreateRequestDto;
import com.example.blog_platform_api.post.dto.request.PostUpdateRequestDto;
import com.example.blog_platform_api.post.dto.response.PostResponseDto;
import com.example.blog_platform_api.post.entity.Post;
import com.example.blog_platform_api.post.mapper.PostMapper;
import com.example.blog_platform_api.post.repository.PostRepository;
import com.example.blog_platform_api.post.service.PostService;
import com.example.blog_platform_api.security.UserPrincipal;
import com.example.blog_platform_api.tag.entity.Tag;
import com.example.blog_platform_api.tag.repository.TagRepository;
import com.example.blog_platform_api.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final CommentRepository commentRepository;
    private final TagRepository tagRepository;



    private User getCurrentUser() {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return principal.getUser();
    }




    private PostResponseDto toDtoWithCommentCount(Post post) {
        long commentCount = commentRepository.countByPostId(post.getId());
        PostResponseDto dto = postMapper.toDto(post);
        return new PostResponseDto(
                dto.id(), dto.title(), dto.content(), dto.status(),
                dto.authorId(), dto.authorUsername(), dto.tags(),
                commentCount, dto.createdAt(), dto.updatedAt()
        );
    }




    @Override
    @Transactional
    public PostResponseDto createPost(PostCreateRequestDto dto) {
        User user = getCurrentUser();
        Post post = postMapper.toEntity(dto, user);
        Post saved = postRepository.save(post);
        return postMapper.toDto(saved);
    }




    @Override
    public PostResponseDto getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));

        if (post.getStatus() == PostStatus.DELETED) {
            throw new ResourceNotFoundException("Post not found with id: " + id);
        }

        return toDtoWithCommentCount(post);
    }




    @Override
    public Page<PostResponseDto> getAllPosts(Pageable pageable) {
        return postRepository.findByStatusOrderByCreatedAtDesc(PostStatus.PUBLISHED, pageable)
                .map(this::toDtoWithCommentCount);
    }




    @Override
    public Page<PostResponseDto> getMyPosts(Pageable pageable) {
        User user = getCurrentUser();
        return postRepository.findByUserIdOrderByCreatedAtDesc(user.getId(), pageable)
                .map(this::toDtoWithCommentCount);
    }




    @Override
    public Page<PostResponseDto> getPostsByUser(Long userId, Pageable pageable) {
        return postRepository.findByUserIdAndStatusOrderByCreatedAtDesc(userId, PostStatus.PUBLISHED, pageable)
                .map(this::toDtoWithCommentCount);
    }




    @Override
    public Page<PostResponseDto> searchPosts(String keyword, Pageable pageable) {
        return postRepository.searchByKeywordAndStatus(keyword, PostStatus.PUBLISHED, pageable)
                .map(this::toDtoWithCommentCount);
    }




    @Override
    @Transactional
    public PostResponseDto updatePost(Long id, PostUpdateRequestDto dto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));

        User user = getCurrentUser();

        if (!post.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("You are not allowed to update this post");
        }

        if (dto.title() != null) post.setTitle(dto.title());
        if (dto.content() != null) post.setContent(dto.content());
        if (dto.status() != null) post.setStatus(dto.status());

        if (dto.tags() != null) {
            post.setTags(resolveTags(dto.tags()));
        }

        return postMapper.toDto(postRepository.save(post));
    }




    @Override
    @Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));

        User user = getCurrentUser();

        boolean isOwner = post.getUser().getId().equals(user.getId());
        boolean isAdmin = user.getRole().name().equals("ADMIN");

        if (!isOwner && !isAdmin) {
            throw new ForbiddenException("You are not allowed to delete this post");
        }

        // Soft delete
        post.setStatus(PostStatus.DELETED);
        postRepository.save(post);
    }




    private Set<Tag> resolveTags(List<String> tagNames) {
        Set<Tag> tags = new HashSet<>();
        for (String name : tagNames) {
            String normalized = name.trim().toLowerCase();
            Tag tag = tagRepository.findByNameIgnoreCase(normalized)
                    .orElseGet(() -> tagRepository.save(new Tag(normalized)));
            tags.add(tag);
        }
        return tags;
    }



}