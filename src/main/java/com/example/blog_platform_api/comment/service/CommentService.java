package com.example.blog_platform_api.comment.service;

import com.example.blog_platform_api.comment.dto.request.CommentRequestDto;
import com.example.blog_platform_api.comment.dto.response.CommentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    CommentResponseDto addComment(Long postId, CommentRequestDto dto);

    Page<CommentResponseDto> getCommentsByPost(Long postId, Pageable pageable);

    CommentResponseDto updateComment(Long commentId, CommentRequestDto dto);

    void deleteComment(Long commentId);
}
