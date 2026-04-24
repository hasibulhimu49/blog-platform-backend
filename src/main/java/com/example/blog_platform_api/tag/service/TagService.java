package com.example.blog_platform_api.tag.service;

import com.example.blog_platform_api.tag.dto.request.TagRequestDto;
import com.example.blog_platform_api.tag.dto.response.TagResponseDto;

import java.util.List;

public interface TagService {
    List<TagResponseDto> getAllTags();
    TagResponseDto createTag(TagRequestDto dto);
    void deleteTag(Long id);
}
