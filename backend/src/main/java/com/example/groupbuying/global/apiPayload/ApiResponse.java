package com.example.groupbuying.global.apiPayload;

import com.example.groupbuying.global.apiPayload.code.BaseErrorCode;
import com.example.groupbuying.global.apiPayload.code.BaseSuccessCode;
import com.example.groupbuying.global.apiPayload.dto.ErrorReason;
import com.example.groupbuying.global.apiPayload.dto.SuccessReason;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ApiResponse<T> {

    private int status;
    private String code;
    private String message;
    private T result;

    public static <T> ApiResponse<T> onSuccess(BaseSuccessCode successCode, T data) {
        SuccessReason reason = successCode.getReason();
        return ApiResponse.<T>builder()
                .status(reason.getStatus())
                .code(reason.getCode())
                .message(reason.getMessage())
                .result(data)
                .build();
    }

    public static <T> ApiResponse<T> onFailure(BaseErrorCode errorCode, T data) {
        ErrorReason reason = errorCode.getReason();
        return ApiResponse.<T>builder()
                .status(reason.getStatus())
                .code(reason.getCode())
                .message(reason.getMessage())
                .result(data)
                .build();
    }
}