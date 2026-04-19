package com.example.blog_platform_api.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private  String message;
    private T data;

    public static <T> ApiResponse<T> success(T data)
    {
        return new ApiResponse<>(true,"Operation successfull",data);

    }

    public static <T> ApiResponse<T> success(String message, T data)
    {
        return new ApiResponse<>(true, message, data);
    }
}