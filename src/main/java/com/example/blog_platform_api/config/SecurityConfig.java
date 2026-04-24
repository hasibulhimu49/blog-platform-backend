package com.example.blog_platform_api.config;

import com.example.blog_platform_api.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
                                                         PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                            JwtAuthenticationFilter jwtAuthFilter) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> {})
            .authorizeHttpRequests(auth -> auth

                // Infrastructure
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers("/").permitAll()

                // Swagger / OpenAPI
                .requestMatchers(
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**",
                        "/v3/api-docs",
                        "/swagger-resources/**",
                        "/webjars/**"
                ).permitAll()

                // Auth — public
                .requestMatchers(HttpMethod.POST, "/api/v1/auth/register").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/auth/me").authenticated()

                // Posts — read is public, write requires USER role
                .requestMatchers(HttpMethod.GET, "/api/v1/posts/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/posts").hasRole("USER")
                .requestMatchers(HttpMethod.PUT, "/api/v1/posts/**").hasRole("USER")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/posts/**").hasAnyRole("USER", "ADMIN")

                // Comments — read is public, write requires authentication
                .requestMatchers(HttpMethod.GET, "/api/v1/posts/*/comments/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/posts/*/comments/**").hasRole("USER")
                .requestMatchers(HttpMethod.PUT, "/api/v1/posts/*/comments/**").hasRole("USER")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/posts/*/comments/**").hasAnyRole("USER", "ADMIN")

                // Tags — read is public, write requires ADMIN
                .requestMatchers(HttpMethod.GET, "/api/v1/tags/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/tags/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/tags/**").hasRole("ADMIN")

                // Users — admin only
                .requestMatchers(HttpMethod.GET, "/api/v1/users/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/api/v1/users/**").hasRole("ADMIN")

                // Everything else requires authentication
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
