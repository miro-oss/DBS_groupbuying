package com.example.groupbuying.global.apiPayload.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SuccessReason {
    private int status;
    private String code;
    private String message;
}