package com.example.blog_platform_api.user.service.impl;


import com.example.blog_platform_api.common.enums.Status;
import com.example.blog_platform_api.common.exception.ResourceNotFoundException;
import com.example.blog_platform_api.user.dto.response.UserResponseDto;
import com.example.blog_platform_api.user.entity.User;
import com.example.blog_platform_api.user.mapper.UserMapper;
import com.example.blog_platform_api.user.repository.UserRepository;
import com.example.blog_platform_api.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    public UserRepository repository;
    public UserMapper mapper;

    //Constructor injection
    UserServiceImpl(UserRepository repository,UserMapper mapper)
    {
        this.repository=repository;
        this.mapper=mapper;
    }



    //Get All User
    public List<UserResponseDto> getAllUser()
    {
        List<User> users=repository.findAll();
        return users.stream().map(user->mapper.toDto(user)).toList();
    }


    //Get User by id

    public UserResponseDto getUserById(Long id)
    {
        User user=repository.findById(id).orElseThrow(()->new ResourceNotFoundException("Can't find any user for id: "+id));
        return mapper.toDto(user);
    }


    //Block User by id
    public void blockUser(Long id)
    {
        User user=repository.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found with id: " + id));
        user.setStatus(Status.BLOCKED);
        repository.save(user);
    }


    //Unblock User by id
    public void unblockUser(Long id)
    {
        User user=repository.findById(id).orElseThrow(()->new ResourceNotFoundException("User not found with id: " + id));
        user.setStatus(Status.ACTIVE);
        repository.save(user);
    }




}
