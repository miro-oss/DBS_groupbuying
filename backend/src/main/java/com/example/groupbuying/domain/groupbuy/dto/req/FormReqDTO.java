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

            @NotNull
            @DecimalMin(value = "0.0", inclusive = false)
            @Digits(integer = 10, fraction = 0, message = "가격은 정수여야 합니다.")
            BigDecimal pricePerUnit,

            String imageUrl,
            @NotNull LocalDateTime orderDate,
            @NotBlank String location,
            @NotNull LocalDateTime tradeTime,

            @NotBlank String accountBank,

            @NotBlank
            @Pattern(regexp = "^[0-9-]*$", message = "계좌번호는 숫자와 하이픈(-)만 입력할 수 있습니다.")
            String accountNumber,

            @NotBlank String accountName,
            @NotNull LocalDateTime deadline
    ) {}

    @Builder
    public record UpdateFormDTO(
            Long categoryId,
            String title,
            String description,

            @DecimalMin(value = "0.0", inclusive = false)
            @Digits(integer = 10, fraction = 0, message = "가격은 정수여야 합니다.")
            BigDecimal pricePerUnit,

            String imageUrl,
            LocalDateTime orderDate,
            String location,
            LocalDateTime tradeTime,
            String accountBank,

            @Pattern(regexp = "^[0-9-]*$", message = "계좌번호는 숫자와 하이픈(-)만 입력할 수 있습니다.")
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