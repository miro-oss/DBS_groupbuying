package com.example.groupbuying.domain.users.service.command;

import com.example.groupbuying.domain.users.dto.req.UsersReqDTO;
import com.example.groupbuying.domain.users.dto.res.UsersResDTO;

public interface UsersCommandService {

    UsersResDTO.SignUpResultDTO signUp(UsersReqDTO.SignUpDTO request);

    UsersResDTO.LoginResultDTO login(UsersReqDTO.LoginDTO request);

    UsersResDTO.ProfileDTO updateProfile(Long userId, UsersReqDTO.UpdateProfileDTO request);

    void deleteUser(Long userId);

}