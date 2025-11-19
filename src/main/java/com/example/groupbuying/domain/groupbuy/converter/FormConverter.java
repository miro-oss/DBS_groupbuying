package com.example.groupbuying.domain.groupbuy.converter;

import com.example.groupbuying.domain.groupbuy.dto.req.FormReqDTO;
import com.example.groupbuying.domain.groupbuy.dto.res.FormResDTO;
import com.example.groupbuying.domain.groupbuy.entity.Category;
import com.example.groupbuying.domain.groupbuy.entity.Form;
import com.example.groupbuying.domain.users.entity.User;

public class FormConverter {

    // Form 생성
    public static Form toFormEntity(User seller, Category category, FormReqDTO.CreateFormDTO dto) {
        return Form.builder()
                .seller(seller)
                .category(category)
                .title(dto.title())
                .description(dto.description())
                .pricePerUnit(dto.pricePerUnit())
                .imageUrl(dto.imageUrl())
                .orderDate(dto.orderDate())
                .location(dto.location())
                .tradeTime(dto.tradeTime())
                .accountBank(dto.accountBank())
                .accountNumber(dto.accountNumber())
                .accountName(dto.accountName())
                .deadline(dto.deadline())
                .build();
    }

    //생성 응답
    public static FormResDTO.CreateFormResultDTO toCreateFormResultDTO(Form form) {
        return FormResDTO.CreateFormResultDTO.builder()
                .formId(form.getId())
                .sellerId(form.getSeller().getId())
                .categoryId(form.getCategory().getId())
                .title(form.getTitle())
                .description(form.getDescription())
                .pricePerUnit(form.getPricePerUnit())
                .imageUrl(form.getImageUrl())
                .orderDate(form.getOrderDate())
                .location(form.getLocation())
                .tradeTime(form.getTradeTime())
                .accountBank(form.getAccountBank())
                .accountNumber(form.getAccountNumber())
                .accountName(form.getAccountName())
                .deadline(form.getDeadline())
                .status(form.getStatus())
                .build();
    }

    // 요약 (리스트)
    public static FormResDTO.FormSummaryDTO toSummaryDTO(Form form) {
        return FormResDTO.FormSummaryDTO.builder()
                .formId(form.getId())
                .title(form.getTitle())
                .pricePerUnit(form.getPricePerUnit())
                .imageUrl(form.getImageUrl())
                .deadline(form.getDeadline())
                .categoryName(form.getCategory().getCategoryName())
                .status(form.getStatus())
                .build();
    }

    // 상세
    public static FormResDTO.FormDetailDTO toDetailDTO(Form form) {
        return FormResDTO.FormDetailDTO.builder()
                .formId(form.getId())
                .sellerId(form.getSeller().getId())
                .sellerNickname(form.getSeller().getNickname())
                .categoryId(form.getCategory().getId())
                .categoryName(form.getCategory().getCategoryName())
                .title(form.getTitle())
                .description(form.getDescription())
                .pricePerUnit(form.getPricePerUnit())
                .imageUrl(form.getImageUrl())
                .orderDate(form.getOrderDate())
                .location(form.getLocation())
                .tradeTime(form.getTradeTime())
                .accountBank(form.getAccountBank())
                .accountNumber(form.getAccountNumber())
                .accountName(form.getAccountName())
                .deadline(form.getDeadline())
                .status(form.getStatus())
                .build();
    }
}