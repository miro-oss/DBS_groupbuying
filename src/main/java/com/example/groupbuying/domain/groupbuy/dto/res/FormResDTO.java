package com.example.groupbuying.domain.groupbuy.dto.res;

import com.example.groupbuying.domain.groupbuy.enums.FormStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FormResDTO {

    @Builder
    public record CreateFormResultDTO(
            Long formId,
            Long sellerId,
            Long categoryId,
            String title,
            String description,
            BigDecimal pricePerUnit,
            String imageUrl,
            LocalDateTime orderDate,
            String location,
            LocalDateTime tradeTime,
            String accountBank,
            String accountNumber,
            String accountName,
            LocalDateTime deadline,
            FormStatus status
    ) {}

    @Builder
    public record FormSummaryDTO(
            Long formId,
            String title,
            BigDecimal pricePerUnit,
            String imageUrl,
            LocalDateTime deadline,
            String categoryName,
            FormStatus status
    ) {}

    @Builder
    public record FormDetailDTO(
            Long formId,
            Long sellerId,
            String sellerNickname,
            Long categoryId,
            String categoryName,
            String title,
            String description,
            BigDecimal pricePerUnit,
            String imageUrl,
            LocalDateTime orderDate,
            String location,
            LocalDateTime tradeTime,
            String accountBank,
            String accountNumber,
            String accountName,
            LocalDateTime deadline,
            FormStatus status
    ) {}
}