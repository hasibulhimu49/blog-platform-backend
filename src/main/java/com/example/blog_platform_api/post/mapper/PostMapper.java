package com.example.blog_platform_api.post.mapper;

import com.example.blog_platform_api.post.dto.request.PostCreateRequestDto;
import com.example.blog_platform_api.post.dto.response.PostResponseDto;
import com.example.blog_platform_api.post.entity.Post;
import com.example.blog_platform_api.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

    public Post toEntity(PostCreateRequestDto dto, User user) {
        Post post = new Post();
        post.setTitle(dto.title());
        post.setContent(dto.content());
        post.setUser(user);
        return post;
    }

    public PostResponseDto toDto(Post post) {
        return new PostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent()
        );
    }
}
