package com.example.groupbuying.domain.groupbuy.exception.code;

import com.example.groupbuying.global.apiPayload.code.BaseSuccessCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SubmissionSuccessCode implements BaseSuccessCode {

    SUBMISSION201_1(HttpStatus.CREATED, "SUBMISSION201_1", "공동구매 신청에 성공했습니다."),
    GET_MY_SUBMISSION_LIST_SUCCESS(HttpStatus.OK, "SUBMISSION200_1", "내가 신청한 공구 목록 조회에 성공했습니다."),
    GET_FORM_SUBMISSION_LIST_SUCCESS(HttpStatus.OK, "SUBMISSION200_2", "해당 모집글의 신청자 목록 조회에 성공했습니다."),
    UPDATE_SUBMISSION_STATUS_SUCCESS(HttpStatus.OK, "SUBMISSION200_3", "신청 상태 변경에 성공했습니다."),
    BULK_UPDATE_SUBMISSION_STATUS_SUCCESS(HttpStatus.OK, "SUBMISSION200_4", "신청 상태 일괄 변경에 성공했습니다."),
    UPDATE_SUBMISSION_INFO_SUCCESS(HttpStatus.OK, "SUBMISSION200_5", "신청 정보 수정에 성공했습니다."),
    GET_MY_SUBMISSION_DETAIL_SUCCESS(HttpStatus.OK, "SUBMISSION200_6", "내 신청 상세 조회에 성공했습니다."),
    GET_FORM_STATS_SUCCESS(HttpStatus.OK, "SUBMISSION200_7", "모집글 통계 요약 조회에 성공했습니다."),



    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

    SubmissionSuccessCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}