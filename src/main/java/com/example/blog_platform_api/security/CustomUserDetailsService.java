package com.example.blog_platform_api.security;


import com.example.blog_platform_api.user.entity.User;
import com.example.blog_platform_api.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user=repository.findByUsername(username).orElseThrow(()->new EntityNotFoundException("Not found"));
        return new UserPrincipal(user);
    }

}
