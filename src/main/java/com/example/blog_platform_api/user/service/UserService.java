package com.example.blog_platform_api.user.service;


import com.example.blog_platform_api.user.dto.response.UserResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

     List<UserResponseDto> getAllUser();
     UserResponseDto getUserById(Long id);
     void blockUser(Long id);
     void unblockUser(Long id);

}
