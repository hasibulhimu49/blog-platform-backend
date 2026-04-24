package com.example.blog_platform_api.common.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiEndpoints {

    private static final String API_VERSION = "/api/v1";

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Auth {
        public static final String BASE = API_VERSION + "/auth";
        public static final String REGISTER = BASE + "/register";
        public static final String LOGIN = BASE + "/login";
        public static final String LOGOUT = BASE + "/logout";
        public static final String ME = BASE + "/me";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Posts {
        public static final String BASE = API_VERSION + "/posts";
        public static final String BY_ID = BASE + "/{id}";
        public static final String MY_POSTS = BASE + "/my";
        public static final String SEARCH = BASE + "/search";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Comments {
        public static final String BASE = API_VERSION + "/posts/{postId}/comments";
        public static final String BY_ID = BASE + "/{commentId}";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Tags {
        public static final String BASE = API_VERSION + "/tags";
        public static final String BY_ID = BASE + "/{id}";
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Users {
        public static final String BASE = API_VERSION + "/users";
        public static final String BY_ID = BASE + "/{id}";
        public static final String BLOCK = BY_ID + "/block";
        public static final String UNBLOCK = BY_ID + "/unblock";
    }
}