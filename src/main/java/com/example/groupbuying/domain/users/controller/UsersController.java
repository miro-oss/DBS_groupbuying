package com.example.groupbuying.domain.users.controller;

import com.example.groupbuying.domain.groupbuy.dto.req.SubmissionReqDTO;
import com.example.groupbuying.domain.groupbuy.dto.res.FormResDTO;
import com.example.groupbuying.domain.groupbuy.dto.res.SubmissionResDTO;
import com.example.groupbuying.domain.groupbuy.exception.code.FormSuccessCode;
import com.example.groupbuying.domain.groupbuy.exception.code.SubmissionSuccessCode;
import com.example.groupbuying.domain.groupbuy.service.command.SubmissionCommandService;
import com.example.groupbuying.domain.groupbuy.service.query.FormQueryService;
import com.example.groupbuying.domain.groupbuy.service.query.SubmissionQueryService;
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
    private final SubmissionQueryService submissionQueryService;
    private final SubmissionCommandService submissionCommandService;

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
        Long userId = SecurityUtil.getCurrentUserId();
        UsersResDTO.ProfileDTO profile = usersQueryService.getProfile(userId);
        return ApiResponse.onSuccess(UsersSuccessCode.GET_PROFILE_SUCCESS, profile);
    }

    @PatchMapping("/profile")
    public ApiResponse<UsersResDTO.ProfileDTO> updateMyProfile(
            @Valid @RequestBody UsersReqDTO.UpdateProfileDTO request
    ) {
        Long userId = SecurityUtil.getCurrentUserId();
        UsersResDTO.ProfileDTO result = usersCommandService.updateProfile(userId, request);
        return ApiResponse.onSuccess(UsersSuccessCode.UPDATE_PROFILE_SUCCESS, result);
    }

    @GetMapping("/forms")
    public ApiResponse<List<FormResDTO.FormSummaryDTO>> getMyForms() {
        Long userId = SecurityUtil.getCurrentUserId();
        List<FormResDTO.FormSummaryDTO> result = formQueryService.getMyForms(userId);
        return ApiResponse.onSuccess(FormSuccessCode.GET_MY_FORM_LIST_SUCCESS, result);
    }

    @GetMapping("/forms/{formId}/submissions")
    public ApiResponse<List<SubmissionResDTO.FormSubmissionDTO>> getFormSubmissionsForMyForm(
            @PathVariable Long formId
    ) {
        Long sellerId = SecurityUtil.getCurrentUserId();
        var result = submissionQueryService.getFormSubmissionsForSeller(sellerId, formId);
        return ApiResponse.onSuccess(
                SubmissionSuccessCode.GET_FORM_SUBMISSION_LIST_SUCCESS,
                result
        );
    }

    @PatchMapping("/forms/{formId}/submissions/{submissionId}/status")
    public ApiResponse<Void> updateSubmissionStatusFromMyPage(
            @PathVariable Long formId,
            @PathVariable Long submissionId,
            @RequestBody SubmissionReqDTO.UpdateSubmissionDTO request
    ) {
        Long sellerId = SecurityUtil.getCurrentUserId();
        submissionCommandService.updateSubmissionStatus(
                sellerId,
                formId,
                submissionId,
                request.paymentStatus()
        );
        return ApiResponse.onSuccess(
                SubmissionSuccessCode.UPDATE_SUBMISSION_STATUS_SUCCESS,
                null
        );
    }

    @PatchMapping("/forms/{formId}/submissions/status/bulk")
    public ApiResponse<Void> bulkUpdateSubmissionStatusFromMyPage(
            @PathVariable Long formId,
            @RequestBody SubmissionReqDTO.BulkUpdateSubmissionDTO request
    ) {
        Long sellerId = SecurityUtil.getCurrentUserId();
        submissionCommandService.bulkUpdateSubmissionStatus(
                sellerId,
                formId,
                request.submissionIds(),
                request.paymentStatus()
        );
        return ApiResponse.onSuccess(
                SubmissionSuccessCode.BULK_UPDATE_SUBMISSION_STATUS_SUCCESS,
                null
        );
    }

    @GetMapping("/submissions")
    public ApiResponse<List<SubmissionResDTO.MySubmissionSummaryDTO>> getMySubmissions() {
        Long userId = SecurityUtil.getCurrentUserId();
        var result = submissionQueryService.getMySubmissions(userId);
        return ApiResponse.onSuccess(
                com.example.groupbuying.domain.groupbuy.exception.code.SubmissionSuccessCode.GET_MY_SUBMISSION_LIST_SUCCESS, result);
    }

    @DeleteMapping
    public ApiResponse<UsersResDTO.DeleteUserResultDTO> deleteMyAccount() {
        Long userId = SecurityUtil.getCurrentUserId();

        usersCommandService.deleteUser(userId);

        UsersResDTO.DeleteUserResultDTO result =
                UsersResDTO.DeleteUserResultDTO.builder()
                        .userId(userId)
                        .build();

        return ApiResponse.onSuccess(UsersSuccessCode.DELETE_USER_SUCCESS, result);
    }

    @PatchMapping("/submissions/{submissionId}")
    public ApiResponse<Void> updateMySubmissionInfo(
            @PathVariable Long submissionId,
            @RequestBody SubmissionReqDTO.UpdateSubmissionInfoDTO request
    ){
        Long buyerId = SecurityUtil.getCurrentUserId();
        submissionCommandService.updateSubmissionInfoByBuyer(buyerId, submissionId, request);
        return ApiResponse.onSuccess(
                SubmissionSuccessCode.UPDATE_SUBMISSION_INFO_SUCCESS, null
        );
    }

    @PostMapping("/submissions/{submissionId}")
    public ApiResponse<SubmissionResDTO.SubmissionDetailDTO> getMySubmissionDetail(
            @PathVariable Long submissionId
    ){
        Long buyerId = SecurityUtil.getCurrentUserId();
        var result = submissionQueryService.getMySubmissionDetail(buyerId, submissionId);
        return ApiResponse.onSuccess(
                SubmissionSuccessCode.GET_MY_SUBMISSION_DETAIL_SUCCESS, result
        );
    }

    @GetMapping("/forms/{formId}/statistics")
    public ApiResponse<SubmissionResDTO.FormStatsDTO> getFormStatistics(
            @PathVariable Long formId
    ) {
        Long sellerId = SecurityUtil.getCurrentUserId();
        var result = submissionQueryService.getFormStatsForSeller(sellerId, formId);
        return ApiResponse.onSuccess(
                SubmissionSuccessCode.GET_FORM_STATS_SUCCESS,
                result
        );
    }

}