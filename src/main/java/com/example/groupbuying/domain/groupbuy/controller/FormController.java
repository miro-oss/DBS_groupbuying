package com.example.groupbuying.domain.groupbuy.controller;

import com.example.groupbuying.domain.groupbuy.dto.req.FormReqDTO;
import com.example.groupbuying.domain.groupbuy.dto.res.FormResDTO;
import com.example.groupbuying.domain.groupbuy.exception.code.FormSuccessCode;
import com.example.groupbuying.domain.groupbuy.service.command.FormCommandService;
import com.example.groupbuying.domain.groupbuy.service.query.FormQueryService;
import com.example.groupbuying.global.apiPayload.ApiResponse;
import com.example.groupbuying.global.security.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/forms")
public class FormController {

    private final FormCommandService formCommandService;
    private final FormQueryService formQueryService;

    @PostMapping
    public ApiResponse<FormResDTO.CreateFormResultDTO> createForm(
            @Valid @RequestBody FormReqDTO.CreateFormDTO request
    ) {
        Long sellerId = SecurityUtil.getCurrentUserId();
        var result = formCommandService.createForm(sellerId, request);
        return ApiResponse.onSuccess(FormSuccessCode.FORM201_1, result);
    }

    @GetMapping
    public ApiResponse<List<FormResDTO.FormSummaryDTO>> getForms(
            @ModelAttribute FormReqDTO.SearchDTO search
    ) {
        var result = formQueryService.getForms(search);
        return ApiResponse.onSuccess(FormSuccessCode.GET_FORM_LIST_SUCCESS, result);
    }

    @GetMapping("/{formId}")
    public ApiResponse<FormResDTO.FormDetailDTO> getFormDetail(
            @PathVariable Long formId
    ) {
        var result = formQueryService.getFormDetail(formId);
        return ApiResponse.onSuccess(FormSuccessCode.GET_FORM_DETAIL_SUCCESS, result);
    }

    @PatchMapping("/{formId}")
    public ApiResponse<FormResDTO.FormDetailDTO> updateForm(
            @PathVariable Long formId,
            @RequestBody FormReqDTO.UpdateFormDTO dto
    ) {
        Long sellerId = SecurityUtil.getCurrentUserId();
        var result = formCommandService.updateForm(sellerId, formId, dto);
        return ApiResponse.onSuccess(FormSuccessCode.UPDATE_FORM_SUCCESS, result);
    }

    @PatchMapping("/{formId}/close")
    public ApiResponse<Void> closeForm(
            @PathVariable Long formId
    ) {
        Long sellerId = SecurityUtil.getCurrentUserId();
        formCommandService.closeForm(sellerId, formId);
        return ApiResponse.onSuccess(FormSuccessCode.CLOSE_FORM_SUCCESS, null);
    }
}