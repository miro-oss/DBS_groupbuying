package com.example.groupbuying.domain.groupbuy.dto.req;

import com.example.groupbuying.domain.groupbuy.enums.PaymentStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

public class SubmissionReqDTO {
    @Builder
    public record CreateSubmissionDTO(
            @NotBlank String buyerName,
            @NotBlank String buyerContact,
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
}
