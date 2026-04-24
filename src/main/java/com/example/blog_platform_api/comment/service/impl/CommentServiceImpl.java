package com.example.blog_platform_api.comment.service.impl;

import com.example.blog_platform_api.comment.dto.request.CommentRequestDto;
import com.example.blog_platform_api.comment.dto.response.CommentResponseDto;
import com.example.blog_platform_api.comment.entity.Comment;
import com.example.blog_platform_api.comment.repository.CommentRepository;
import com.example.blog_platform_api.comment.service.CommentService;
import com.example.blog_platform_api.common.enums.PostStatus;
import com.example.blog_platform_api.common.exception.BadRequestException;
import com.example.blog_platform_api.common.exception.ForbiddenException;
import com.example.blog_platform_api.common.exception.ResourceNotFoundException;
import com.example.blog_platform_api.post.entity.Post;
import com.example.blog_platform_api.post.repository.PostRepository;
import com.example.blog_platform_api.security.UserPrincipal;
import com.example.blog_platform_api.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;



    private User getCurrentUser() {
        UserPrincipal principal = (UserPrincipal) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return principal.getUser();
    }



    private CommentResponseDto toDto(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getId(),
                comment.getUser().getUsername(),
                comment.getPost().getId(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }



    @Override
    @Transactional
    public CommentResponseDto addComment(Long postId, CommentRequestDto dto) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + postId));

        if (post.getStatus() == PostStatus.DELETED || post.getStatus() == PostStatus.DRAFT) {
            throw new BadRequestException("Cannot comment on a post that is not published");
        }

        Comment comment = new Comment();
        comment.setContent(dto.content());
        comment.setUser(getCurrentUser());
        comment.setPost(post);

        return toDto(commentRepository.save(comment));
    }




    @Override
    public Page<CommentResponseDto> getCommentsByPost(Long postId, Pageable pageable) {
        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("Post not found with id: " + postId);
        }
        return commentRepository.findByPostIdOrderByCreatedAtDesc(postId, pageable)
                .map(this::toDto);
    }




    @Override
    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto dto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));

        User currentUser = getCurrentUser();

        if (!comment.getUser().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("You are not allowed to update this comment");
        }

        comment.setContent(dto.content());
        return toDto(commentRepository.save(comment));
    }




    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));

        User currentUser = getCurrentUser();

        boolean isOwner = comment.getUser().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole().name().equals("ADMIN");

        if (!isOwner && !isAdmin) {
            throw new ForbiddenException("You are not allowed to delete this comment");
        }

        commentRepository.delete(comment);
    }



}
