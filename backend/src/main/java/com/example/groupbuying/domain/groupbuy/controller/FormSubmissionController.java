package com.example.groupbuying.domain.groupbuy.controller;

import com.example.groupbuying.domain.groupbuy.dto.req.SubmissionReqDTO;
import com.example.groupbuying.domain.groupbuy.dto.res.SubmissionResDTO;
import com.example.groupbuying.domain.groupbuy.exception.code.SubmissionSuccessCode;
import com.example.groupbuying.domain.groupbuy.service.command.SubmissionCommandService;
import com.example.groupbuying.domain.groupbuy.service.query.SubmissionQueryService;
import com.example.groupbuying.global.apiPayload.ApiResponse;
import com.example.groupbuying.global.security.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/forms/{formId}/submissions")
public class FormSubmissionController {

    private final SubmissionCommandService submissionCommandService;

    @PostMapping
    public ApiResponse<SubmissionResDTO.CreateSubmissionResultDTO> createSubmission(
            @PathVariable Long formId,
            @Valid @RequestBody SubmissionReqDTO.CreateSubmissionDTO request
    ) {
        Long buyerId = SecurityUtil.getCurrentUserId();
        var result = submissionCommandService.createSubmission(formId, buyerId, request);
        return ApiResponse.onSuccess(SubmissionSuccessCode.SUBMISSION201_1, result);
    }
}