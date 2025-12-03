package com.example.groupbuying.global.apiPayload.code;

import com.example.groupbuying.global.apiPayload.dto.ErrorReason;
import org.springframework.http.HttpStatus;

public interface BaseErrorCode {

    HttpStatus getStatus();
    String getCode();
    String getMessage();

    default ErrorReason getReason() {
        return ErrorReason.builder()
                .status(getStatus().value())
                .code(getCode())
                .message(getMessage())
                .build();
    }

    default ErrorReason getReasonHttpStatus() {
        return getReason();
    }
}