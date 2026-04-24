package com.example.blog_platform_api.tag.service.impl;

import com.example.blog_platform_api.common.exception.DuplicateResourceException;
import com.example.blog_platform_api.common.exception.ResourceNotFoundException;
import com.example.blog_platform_api.tag.dto.request.TagRequestDto;
import com.example.blog_platform_api.tag.dto.response.TagResponseDto;
import com.example.blog_platform_api.tag.entity.Tag;
import com.example.blog_platform_api.tag.repository.TagRepository;
import com.example.blog_platform_api.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public List<TagResponseDto> getAllTags() {
        return tagRepository.findAll()
                .stream()
                .map(tag -> new TagResponseDto(tag.getId(), tag.getName(), tag.getPosts().size()))
                .toList();
    }

    @Override
    @Transactional
    public TagResponseDto createTag(TagRequestDto dto) {
        String normalizedName = dto.name().trim().toLowerCase();
        if (tagRepository.existsByNameIgnoreCase(normalizedName)) {
            throw new DuplicateResourceException("Tag '" + normalizedName + "' already exists");
        }
        Tag saved = tagRepository.save(new Tag(normalizedName));
        return new TagResponseDto(saved.getId(), saved.getName(), 0);
    }

    @Override
    @Transactional
    public void deleteTag(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + id));
        tagRepository.delete(tag);
    }
}
