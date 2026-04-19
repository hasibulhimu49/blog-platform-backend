package com.example.blog_platform_api.user.mapper;


import com.example.blog_platform_api.user.dto.response.UserResponseDto;
import com.example.blog_platform_api.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

/*    public User toEntity(UserCreateRequestDto dto)
    {
        User user=new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        user.setRole(dto.getRole());
        return user ;

    }*/

    public UserResponseDto toDto(User user)
    {
        UserResponseDto responseDto=new UserResponseDto();
        responseDto.setId(user.getId());
        responseDto.setName(user.getName());
        responseDto.setUsername(user.getUsername());
        responseDto.setEmail(user.getEmail());
        responseDto.setRole(user.getRole());
        responseDto.setStatus(user.getStatus());
        return responseDto;
    }
}
