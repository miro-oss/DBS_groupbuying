package com.example.groupbuying.global.apiPayload.code;

import com.example.groupbuying.global.apiPayload.dto.SuccessReason;
import org.springframework.http.HttpStatus;

public interface BaseSuccessCode {

    HttpStatus getStatus();
    String getCode();
    String getMessage();

    default SuccessReason getReason() {
        return SuccessReason.builder()
                .status(getStatus().value())
                .code(getCode())
                .message(getMessage())
                .build();
    }

    default SuccessReason getReasonHttpStatus() {
        return getReason();
    }
}