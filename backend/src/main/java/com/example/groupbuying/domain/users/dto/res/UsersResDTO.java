package com.example.groupbuying.domain.users.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class UsersResDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class SignUpResultDTO {
        private Long userId;
        private String nickname;
        private String email;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class LoginResultDTO {
        private Long userId;
        private String nickname;
        private String email;
        private String accessToken;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class ProfileDTO {
        private Long userId;
        private String nickname;
        private String email;
        private String phone;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class DeleteUserResultDTO {
        private Long userId;
    }

}