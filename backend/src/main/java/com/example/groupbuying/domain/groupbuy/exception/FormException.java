package com.example.groupbuying.domain.groupbuy.exception;

import com.example.groupbuying.global.apiPayload.code.BaseErrorCode;
import com.example.groupbuying.global.apiPayload.exception.GeneralException;

public class FormException extends GeneralException {

  public FormException(BaseErrorCode code) {
    super(code);
  }
}