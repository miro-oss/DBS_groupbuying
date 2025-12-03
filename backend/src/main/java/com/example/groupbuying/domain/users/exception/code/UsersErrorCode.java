package com.example.groupbuying.domain.users.exception.code;

import com.example.groupbuying.global.apiPayload.code.BaseErrorCode;
import com.example.groupbuying.global.apiPayload.dto.ErrorReason;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UsersErrorCode implements BaseErrorCode {

    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST,
            "USER400_1",
            "이미 사용 중인 이메일입니다."),
    INVALID_EMAIL_DOMAIN(HttpStatus.BAD_REQUEST,
            "USER400_2",
            "허용되지 않은 이메일 도메인입니다."),

    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED,
            "USER401_1",
            "이메일 또는 비밀번호가 올바르지 않습니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN,
            "USER403_1",
            "해당 리소스에 대한 권한이 없습니다."),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND,
            "USER404_1",
            "해당 사용자를 찾을 수 없습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

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