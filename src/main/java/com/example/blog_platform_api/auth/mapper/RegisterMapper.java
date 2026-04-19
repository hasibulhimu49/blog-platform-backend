package com.example.blog_platform_api.auth.mapper;

import com.example.blog_platform_api.auth.dto.request.RegisterRequest;
import com.example.blog_platform_api.auth.dto.response.RegisterResponse;
import com.example.blog_platform_api.user.entity.User;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
//@Component
public interface /*class*/ RegisterMapper {

    //@Mapping(source = "userName",target = "username") if field name is different
    //@Mapping(source = "userEmail",target = "username")
    public User toEntity(RegisterRequest requestDto);
   /* {
        User user= new User();

        user.setUsername(requestDto.getUsername());
        user.setEmail(requestDto.getEmail());
        return user;
    }*/

    public RegisterResponse toDto(User user);
   /* {
        RegisterResponse response=new RegisterResponse();
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        return response;

    }*/


}
