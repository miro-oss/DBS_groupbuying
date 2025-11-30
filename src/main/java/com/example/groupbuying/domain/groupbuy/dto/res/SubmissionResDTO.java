package com.example.groupbuying.domain.groupbuy.dto.res;

import com.example.groupbuying.domain.groupbuy.enums.PaymentStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class SubmissionResDTO {

    @Builder
    public record CreateSubmissionResultDTO(
            Long submissionId,
            Long formId,
            Long buyerId,
            String buyerName,
            String buyerContact,
            int quantity,
            PaymentStatus paymentStatus,
            String accountBank,
            String accountNumber,
            String accountName
    ) {}

    @Builder
    public record MySubmissionSummaryDTO(
            Long submissionId,
            Long formId,
            String formTitle,
            String formImageUrl,
            BigDecimal pricePerUnit,
            int quantity,
            PaymentStatus paymentStatus,
            LocalDateTime deadline,
            LocalDateTime submittedAt
    ) {}

    @Builder
    public record FormSubmissionDTO(
            Long submissionId,
            Long buyerId,
            String buyerName,
            int quantity,
            PaymentStatus paymentStatus,
            LocalDateTime submittedAt
    ) {}

    @Builder
    public record SubmissionDetailDTO(
            Long submissionId,
            Long formId,
            String formTitle,
            String formImageUrl,
            BigDecimal pricePerUnit,
            Long buyerId,
            String buyerName,
            String buyerContact,
            int quantity,
            PaymentStatus paymentStatus,
            LocalDateTime submittedAt,
            String accountBank,
            String accountNumber,
            String accountName
    ){}

    @Builder
    public record PaymentStatusStatDTO(
            PaymentStatus status,
            long count,
            long totalQuantity
    ){}

    @Builder
    public record FormStatsDTO(
            Long formId,
            int totalSubmissions,
            long totalQuantity,
            List<PaymentStatusStatDTO> statusStats
    ){}
}