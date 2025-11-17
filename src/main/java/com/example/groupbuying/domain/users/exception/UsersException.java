package com.example.groupbuying.domain.users.exception;

import com.example.groupbuying.domain.users.exception.code.UsersErrorCode;
import com.example.groupbuying.global.apiPayload.exception.GeneralException;

public class UsersException extends GeneralException {

  public UsersException(UsersErrorCode errorCode) {
    super(errorCode);
  }
}