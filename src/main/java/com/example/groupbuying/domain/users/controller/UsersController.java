package com.example.groupbuying.domain.users.controller;

import com.example.groupbuying.domain.users.dto.req.UsersReqDTO;
import com.example.groupbuying.domain.users.dto.res.UsersResDTO;
import com.example.groupbuying.domain.users.exception.code.UsersSuccessCode;
import com.example.groupbuying.domain.users.service.command.UsersCommandService;
import com.example.groupbuying.domain.users.service.query.UsersQueryService;
import com.example.groupbuying.global.apiPayload.ApiResponse;
import com.example.groupbuying.global.security.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UsersController {

    private final UsersCommandService usersCommandService;
    private final UsersQueryService usersQueryService;
    private final SecurityUtil securityUtil;

    @PostMapping("/signup")
    public ApiResponse<UsersResDTO.SignUpResultDTO> signUp(
            @Valid @RequestBody UsersReqDTO.SignUpDTO request
    ) {
        UsersResDTO.SignUpResultDTO result = usersCommandService.signUp(request);
        return ApiResponse.onSuccess(UsersSuccessCode.SIGN_UP_SUCCESS, result);
    }

    @PostMapping("/login")
    public ApiResponse<UsersResDTO.LoginResultDTO> login(
            @Valid @RequestBody UsersReqDTO.LoginDTO request
    ) {
        UsersResDTO.LoginResultDTO result = usersCommandService.login(request);
        return ApiResponse.onSuccess(UsersSuccessCode.LOGIN_SUCCESS, result);
    }

    @GetMapping("/profile")
    public ApiResponse<UsersResDTO.ProfileDTO> getMyProfile() {
        Long userId = securityUtil.getCurrentUserId();
        UsersResDTO.ProfileDTO profile = usersQueryService.getProfile(userId);
        return ApiResponse.onSuccess(UsersSuccessCode.GET_PROFILE_SUCCESS, profile);
    }
}