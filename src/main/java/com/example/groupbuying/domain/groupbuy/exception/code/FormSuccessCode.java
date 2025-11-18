package com.example.groupbuying.domain.groupbuy.exception.code;

import com.example.groupbuying.global.apiPayload.code.BaseSuccessCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum FormSuccessCode implements BaseSuccessCode {

    FORM201_1(HttpStatus.CREATED, "FORM201_1", "모집글 등록에 성공했습니다."),
    GET_FORM_LIST_SUCCESS(HttpStatus.OK, "FORM200_1", "모집글 목록 조회에 성공했습니다."),
    GET_FORM_DETAIL_SUCCESS(HttpStatus.OK, "FORM200_2", "모집글 상세 조회에 성공했습니다."),
    GET_MY_FORM_LIST_SUCCESS(HttpStatus.OK, "FORM200_3", "내 모집글 목록 조회에 성공했습니다."),
    UPDATE_FORM_SUCCESS(HttpStatus.OK, "FORM200_4", "모집글 수정에 성공했습니다."),
    CLOSE_FORM_SUCCESS(HttpStatus.OK, "FORM200_5", "모집글 마감에 성공했습니다.")
    ;


    private final HttpStatus status;
    private final String code;
    private final String message;

    FormSuccessCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}