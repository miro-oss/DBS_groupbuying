package com.example.groupbuying.domain.groupbuy.exception;

import com.example.groupbuying.domain.groupbuy.exception.code.SubmissionErrorCode;
import com.example.groupbuying.global.apiPayload.exception.GeneralException;

public class SubmissionException extends GeneralException {

    public SubmissionException(SubmissionErrorCode errorCode) {
        super(errorCode);
    }
}