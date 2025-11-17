package com.example.groupbuying.domain.users.exception.code;

import com.example.groupbuying.global.apiPayload.code.BaseSuccessCode;
import com.example.groupbuying.global.apiPayload.dto.SuccessReason;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UsersSuccessCode implements BaseSuccessCode {

    SIGN_UP_SUCCESS(HttpStatus.CREATED,
            "USER201_1",
            "회원가입에 성공했습니다."),

    LOGIN_SUCCESS(HttpStatus.OK,
            "USER200_1",
            "로그인에 성공했습니다."),

    GET_PROFILE_SUCCESS(HttpStatus.OK,
            "USER200_2",
            "회원 정보를 성공적으로 조회했습니다."),

    UPDATE_PROFILE_SUCCESS(HttpStatus.OK,
            "USER200_3",
            "회원 정보가 성공적으로 수정되었습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

    @Override
    public SuccessReason getReason() {
        return SuccessReason.builder()
                .status(status.value())
                .code(code)
                .message(message)
                .build();
    }

    @Override
    public SuccessReason getReasonHttpStatus() {
        return getReason();
    }
}