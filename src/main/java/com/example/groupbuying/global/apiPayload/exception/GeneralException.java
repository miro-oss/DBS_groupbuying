package com.example.groupbuying.global.apiPayload.exception;

import com.example.groupbuying.global.apiPayload.code.BaseErrorCode;
import lombok.Getter;


@Getter
public class GeneralException extends RuntimeException {

    private final BaseErrorCode errorCode;

    public GeneralException(BaseErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}