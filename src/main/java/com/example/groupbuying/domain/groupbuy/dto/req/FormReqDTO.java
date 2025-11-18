package com.example.groupbuying.domain.groupbuy.dto.req;

import jakarta.validation.constraints.*;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FormReqDTO {

    @Builder
    public record CreateFormDTO(
            @NotNull Long categoryId,
            @NotBlank String title,
            @NotBlank String description,
            @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal pricePerUnit,
            String imageUrl,
            @NotNull LocalDateTime orderDate,
            @NotBlank String location,
            @NotNull LocalDateTime tradeTime,
            @NotBlank String accountBank,
            @NotBlank String accountNumber,
            @NotBlank String accountName,
            @NotNull LocalDateTime deadline
    ) {}

    @Builder
    public record UpdateFormDTO(
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
            LocalDateTime deadline
    ) {}

    @Builder
    public record SearchDTO(
            Long categoryId,
            String status,
            String keyword
    ) {}
}