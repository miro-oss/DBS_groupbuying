package com.example.groupbuying.domain.users.converter;

import com.example.groupbuying.domain.users.dto.req.UsersReqDTO;
import com.example.groupbuying.domain.users.dto.res.UsersResDTO;
import com.example.groupbuying.domain.users.entity.User;

public class UsersConverter {

    public static User toUser(UsersReqDTO.SignUpDTO dto, String encodedPassword) {
        return User.builder()
                .nickname(dto.getNickname())
                .email(dto.getEmail())
                .password(encodedPassword)
                .phone(dto.getPhone())
                .build();
    }

    public static UsersResDTO.SignUpResultDTO toSignUpResultDTO(User user) {
        return UsersResDTO.SignUpResultDTO.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .build();
    }

    public static UsersResDTO.LoginResultDTO toLoginResultDTO(User user, String accessToken) {
        return UsersResDTO.LoginResultDTO.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .accessToken(accessToken)
                .build();
    }

    public static UsersResDTO.ProfileDTO toProfileDTO(User user) {
        return UsersResDTO.ProfileDTO.builder()
                .userId(user.getId())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
    }
}