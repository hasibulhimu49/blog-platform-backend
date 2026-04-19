package com.example.blog_platform_api.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;



    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)throws IOException, ServletException
    {

        String path = request.getServletPath();

        // Skip Swagger/OpenAPI
        if (path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs") || path.equals("/swagger-ui.html")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader=request.getHeader("Authorization");
       // log.info(authHeader);
        log.info("Authorization Header: {}", authHeader); //best practice

                if(authHeader==null || !authHeader.startsWith("Bearer "))
                {
                    filterChain.doFilter(request,response);
                    return;
                }


                String token=authHeader.substring(7);
                log.info(token);
                String username=jwtService.extractUsername(token);
                log.info(username);

                if(username!=null&& SecurityContextHolder.getContext().getAuthentication()==null)
                {
                    UserDetails userDetails= customUserDetailsService.loadUserByUsername(username);
                   // System.out.println("Debug Username: " + userDetails.getUsername());
                   // System.out.println("Debug Password: " + userDetails.getPassword());
                   // System.out.println("Debug Authorities: " + userDetails.getAuthorities());
                    if(jwtService.isTokenValid(token,userDetails))
                    {
                        UsernamePasswordAuthenticationToken authToken
                                =new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authToken);


                    }
                }
                filterChain.doFilter(request,response);

    }





}
