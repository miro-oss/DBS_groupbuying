package com.example.groupbuying.domain.users.controller;

import com.example.groupbuying.domain.groupbuy.dto.res.FormResDTO;
import com.example.groupbuying.domain.groupbuy.exception.code.FormSuccessCode;
import com.example.groupbuying.domain.groupbuy.service.query.FormQueryService;
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

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UsersController {

    private final UsersCommandService usersCommandService;
    private final UsersQueryService usersQueryService;
    private final FormQueryService formQueryService;


    // 회원가입
    @PostMapping("/signup")
    public ApiResponse<UsersResDTO.SignUpResultDTO> signUp(
            @Valid @RequestBody UsersReqDTO.SignUpDTO request
    ) {
        UsersResDTO.SignUpResultDTO result = usersCommandService.signUp(request);
        return ApiResponse.onSuccess(UsersSuccessCode.SIGN_UP_SUCCESS, result);
    }

    // 로그인
    @PostMapping("/login")
    public ApiResponse<UsersResDTO.LoginResultDTO> login(
            @Valid @RequestBody UsersReqDTO.LoginDTO request
    ) {
        UsersResDTO.LoginResultDTO result = usersCommandService.login(request);
        return ApiResponse.onSuccess(UsersSuccessCode.LOGIN_SUCCESS, result);
    }

    // 조회
    @GetMapping("/profile")
    public ApiResponse<UsersResDTO.ProfileDTO> getMyProfile() {
        Long userId = SecurityUtil.getCurrentUserId();
        UsersResDTO.ProfileDTO profile = usersQueryService.getProfile(userId);
        return ApiResponse.onSuccess(UsersSuccessCode.GET_PROFILE_SUCCESS, profile);
    }

    // 수정
    @PatchMapping("/profile")
    public ApiResponse<UsersResDTO.ProfileDTO> updateMyProfile(
            @Valid @RequestBody UsersReqDTO.UpdateProfileDTO request
    ) {
        Long userId = SecurityUtil.getCurrentUserId();
        UsersResDTO.ProfileDTO result = usersCommandService.updateProfile(userId, request);
        return ApiResponse.onSuccess(UsersSuccessCode.UPDATE_PROFILE_SUCCESS, result);
    }

    // 내가 올린 모집글 보기
    @GetMapping("/forms")
    public ApiResponse<List<FormResDTO.FormSummaryDTO>> getMyForms() {
        Long userId = SecurityUtil.getCurrentUserId();
        List<FormResDTO.FormSummaryDTO> result = formQueryService.getMyForms(userId);
        return ApiResponse.onSuccess(FormSuccessCode.GET_MY_FORM_LIST_SUCCESS, result);
    }
}