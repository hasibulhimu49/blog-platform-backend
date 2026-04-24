package com.example.blog_platform_api.auth.controller;

import com.example.blog_platform_api.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Hidden
@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<ApiResponse<Map<String, Object>>> home() {
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("name", "Blog Platform API");
        info.put("version", "1.0.0");
        info.put("status", "running");
        info.put("timestamp", LocalDateTime.now());
        info.put("documentation", "/swagger-ui/index.html");
        info.put("apiDocs", "/v3/api-docs");
        info.put("health", "/actuator/health");
        info.put("github", "https://github.com/hasibulhimu49/blog-platform-backend");
        info.put("developer", "Mohammad Hasibul Hasan");
        return ResponseEntity.ok(ApiResponse.success("Blog Platform API is running", info));
    }
}