package com.example.groupbuying.global.security;

import com.example.groupbuying.global.apiPayload.exception.GeneralException;
import com.example.groupbuying.global.apiPayload.code.GeneralErrorCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    private SecurityUtil() {}

    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new GeneralException(GeneralErrorCode.UNAUTHORIZED);
        }

        Object principal = authentication.getPrincipal();

        if (principal == null || "anonymousUser".equals(principal)) {
            throw new GeneralException(GeneralErrorCode.UNAUTHORIZED);
        }

        String userIdStr = authentication.getName();

        try {
            return Long.parseLong(userIdStr);
        } catch (NumberFormatException e) {
            throw new GeneralException(GeneralErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}