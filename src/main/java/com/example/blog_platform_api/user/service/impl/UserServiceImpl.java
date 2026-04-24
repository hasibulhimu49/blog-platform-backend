package com.example.blog_platform_api.user.service.impl;

import com.example.blog_platform_api.common.enums.Status;
import com.example.blog_platform_api.common.exception.ResourceNotFoundException;
import com.example.blog_platform_api.user.dto.response.UserResponseDto;
import com.example.blog_platform_api.user.entity.User;
import com.example.blog_platform_api.user.mapper.UserMapper;
import com.example.blog_platform_api.user.repository.UserRepository;
import com.example.blog_platform_api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public List<UserResponseDto> getAllUser() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public UserResponseDto getUserById(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return mapper.toDto(user);
    }

    @Override
    @Transactional
    public void blockUser(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        user.setStatus(Status.BLOCKED);
        repository.save(user);
    }

    @Override
    @Transactional
    public void unblockUser(Long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        user.setStatus(Status.ACTIVE);
        repository.save(user);
    }
}
