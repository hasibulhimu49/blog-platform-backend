package com.example.blog_platform_api.auth.service.impl;


import com.example.blog_platform_api.auth.dto.request.LoginRequest;
import com.example.blog_platform_api.auth.dto.request.RegisterRequest;
import com.example.blog_platform_api.auth.dto.response.AuthResponse;
import com.example.blog_platform_api.auth.dto.response.RegisterResponse;
import com.example.blog_platform_api.auth.mapper.RegisterMapper;
import com.example.blog_platform_api.auth.service.AuthService;
import com.example.blog_platform_api.common.enums.Role;
import com.example.blog_platform_api.common.enums.Status;
import com.example.blog_platform_api.security.JwtService;
import com.example.blog_platform_api.security.UserPrincipal;
import com.example.blog_platform_api.user.entity.User;
import com.example.blog_platform_api.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {


    private final RegisterMapper mapper;
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;




    public RegisterResponse register(RegisterRequest request){

        User user=mapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIVE);
        User saved=repository.save(user);
        return mapper.toDto(saved);

    }





    public AuthResponse login(LoginRequest loginRequest) {

        User user = repository.findByUsername(loginRequest.username())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        } else if (user.getStatus()==Status.BLOCKED) {
            throw new RuntimeException("Your account is blocked!");
        }

       String token = jwtService.generateToken(new UserPrincipal(user));


        return new AuthResponse("Login successful", token);
        //return new AuthResponse("Login successful", null);
    }






    public void logout(){

    }



}
