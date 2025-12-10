package com.example.groupbuying.domain.groupbuy.dto.req;

import com.example.groupbuying.domain.groupbuy.enums.PaymentStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import java.util.List;

public class SubmissionReqDTO {
    @Builder
    public record CreateSubmissionDTO(
            @NotBlank String buyerName,

            @NotBlank
            @Pattern(regexp = "^[0-9-]*$", message = "전화번호는 숫자와 하이픈(-)만 입력할 수 있습니다.")
            String buyerContact,

            @Min(1) int quantity
    ){}

    @Builder
    public record UpdateSubmissionDTO(
            @NotNull PaymentStatus paymentStatus
    ){}

    @Builder
    public record BulkUpdateSubmissionDTO(
            @NotEmpty List<Long> submissionIds,
            @NotNull PaymentStatus paymentStatus
    ){}

    @Builder
    public record UpdateSubmissionInfoDTO(
            String buyerName,

            @Pattern(regexp = "^[0-9-]*$", message = "전화번호는 숫자와 하이픈(-)만 입력할 수 있습니다.")
            String buyerContact,

            @Min(1) int quantity
    ){}
}