package com.example.blog_platform_api.post.mapper;

import com.example.blog_platform_api.common.enums.PostStatus;
import com.example.blog_platform_api.post.dto.request.PostCreateRequestDto;
import com.example.blog_platform_api.post.dto.response.PostResponseDto;
import com.example.blog_platform_api.post.entity.Post;
import com.example.blog_platform_api.tag.entity.Tag;
import com.example.blog_platform_api.tag.repository.TagRepository;
import com.example.blog_platform_api.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PostMapper {

    private final TagRepository tagRepository;

    public Post toEntity(PostCreateRequestDto dto, User user) {
        Post post = new Post();
        post.setTitle(dto.title());
        post.setContent(dto.content());
        post.setUser(user);
        post.setStatus(dto.status() != null ? dto.status() : PostStatus.PUBLISHED);

        if (dto.tags() != null && !dto.tags().isEmpty()) {
            Set<Tag> tagEntities = resolveTags(dto.tags());
            post.setTags(tagEntities);
        }

        return post;
    }

    public PostResponseDto toDto(Post post) {
        Set<String> tagNames = post.getTags() == null ? Set.of() :
                post.getTags().stream().map(Tag::getName).collect(Collectors.toSet());

        return new PostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getStatus(),
                post.getUser().getId(),
                post.getUser().getUsername(),
                tagNames,
                0L, // comment count injected by service
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
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
