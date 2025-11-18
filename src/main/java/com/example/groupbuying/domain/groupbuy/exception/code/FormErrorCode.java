package com.example.groupbuying.domain.groupbuy.exception.code;

import com.example.groupbuying.global.apiPayload.code.BaseErrorCode;
import com.example.groupbuying.global.apiPayload.dto.ErrorReason;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum FormErrorCode implements BaseErrorCode {

    FORM404_1(HttpStatus.NOT_FOUND, "FORM404_1", "판매자를 찾을 수 없습니다."),
    FORM404_2(HttpStatus.NOT_FOUND, "FORM404_2", "해당 카테고리를 찾을 수 없습니다."),
    FORM_NOT_FOUND(HttpStatus.NOT_FOUND, "FORM404_1", "해당 모집글을 찾을 수 없습니다."),
    FORM_FORBIDDEN(HttpStatus.FORBIDDEN, "FORM403_1", "해당 모집글에 대한 권한이 없습니다.")
    ;


    private final HttpStatus status;
    private final String code;
    private final String message;

    FormErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    @Override
    public ErrorReason getReason() {
        return ErrorReason.builder()
                .status(status.value())
                .code(code)
                .message(message)
                .build();
    }

    @Override
    public ErrorReason getReasonHttpStatus() {
        return getReason();
    }
}