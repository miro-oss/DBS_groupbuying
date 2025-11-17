package com.example.groupbuying.global.security;

import com.example.groupbuying.global.apiPayload.exception.GeneralException;
import com.example.groupbuying.global.apiPayload.code.GeneralErrorCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증 정보 없는 경우
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new GeneralException(GeneralErrorCode.UNAUTHORIZED);
        }

        Object principal = authentication.getPrincipal();

        // anonymousUser 처리
        if (principal == null || "anonymousUser".equals(principal)) {
            throw new GeneralException(GeneralErrorCode.UNAUTHORIZED);
        }

        String userIdStr = authentication.getName(); // == principal.toString()

        try {
            return Long.parseLong(userIdStr);
        } catch (NumberFormatException e) {
            throw new GeneralException(GeneralErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}