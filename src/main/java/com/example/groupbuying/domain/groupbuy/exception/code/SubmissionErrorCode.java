package com.example.groupbuying.domain.groupbuy.exception.code;

import com.example.groupbuying.global.apiPayload.code.BaseErrorCode;
import com.example.groupbuying.global.apiPayload.dto.ErrorReason;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SubmissionErrorCode implements BaseErrorCode {

    SUBMISSION404_1(HttpStatus.NOT_FOUND, "SUBMISSION404_1", "해당 신청을 찾을 수 없습니다."),
    SUBMISSION404_2(HttpStatus.NOT_FOUND, "SUBMISSION404_2", "구매자를 찾을 수 없습니다."),
    SUBMISSION403_1(HttpStatus.FORBIDDEN, "SUBMISSION403_1", "해당 신청에 대한 권한이 없습니다."),
    SUBMISSION409_1(HttpStatus.CONFLICT, "SUBMISSION409_1", "이미 해당 모집글에 신청한 사용자입니다."),
    SUBMISSION409_2(HttpStatus.CONFLICT, "SUBMISSION409_2", "입금대기 상태에서만 수정할 수 있습니다."),
    CANNOT_CHANGE_CANCELED(HttpStatus.BAD_REQUEST, "SUBMISSION400_1", "이미 취소된 거래는 상태를 변경할 수 없습니다.")
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

    SubmissionErrorCode(HttpStatus status, String code, String message) {
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