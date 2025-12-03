package com.example.groupbuying.domain.groupbuy.converter;

import com.example.groupbuying.domain.groupbuy.dto.req.SubmissionReqDTO;
import com.example.groupbuying.domain.groupbuy.dto.res.SubmissionResDTO;
import com.example.groupbuying.domain.groupbuy.entity.Form;
import com.example.groupbuying.domain.groupbuy.entity.Submission;
import com.example.groupbuying.domain.users.entity.User;

public class SubmissionConverter {

    public static Submission toSubmissionEntity(
            Form form,
            User buyer,
            SubmissionReqDTO.CreateSubmissionDTO dto
    ) {
        return Submission.builder()
                .form(form)
                .buyer(buyer)
                .buyerName(dto.buyerName())
                .buyerContact(dto.buyerContact())
                .quantity(dto.quantity())
                .build();
    }

    public static SubmissionResDTO.CreateSubmissionResultDTO toCreateSubmissionResultDTO(Submission submission) {
        Form form = submission.getForm();

        return SubmissionResDTO.CreateSubmissionResultDTO.builder()
                .submissionId(submission.getId())
                .formId(form.getId())
                .buyerId(submission.getBuyer().getId())
                .buyerName(submission.getBuyerName())
                .buyerContact(submission.getBuyerContact())
                .quantity(submission.getQuantity())
                .paymentStatus(submission.getPaymentStatus())
                .accountBank(form.getAccountBank())
                .accountNumber(form.getAccountNumber())
                .accountName(form.getAccountName())
                .build();
    }

    public static SubmissionResDTO.MySubmissionSummaryDTO toMySubmissionSummaryDTO(Submission submission) {
        Form form = submission.getForm();

        return SubmissionResDTO.MySubmissionSummaryDTO.builder()
                .submissionId(submission.getId())
                .formId(form.getId())
                .formTitle(form.getTitle())
                .formImageUrl(form.getImageUrl())
                .pricePerUnit(form.getPricePerUnit())
                .quantity(submission.getQuantity())
                .paymentStatus(submission.getPaymentStatus())
                .deadline(form.getDeadline())
                .submittedAt(submission.getCreatedAt())
                .accountBank(form.getAccountBank())
                .accountNumber(form.getAccountNumber())
                .accountName(form.getAccountName())
                .location(form.getLocation())
                .tradeTime(form.getTradeTime())
                .sellerPhone(form.getSeller().getPhone())
                .build();
    }

    public static SubmissionResDTO.FormSubmissionDTO toFormSubmissionDTO(Submission submission) {
        return SubmissionResDTO.FormSubmissionDTO.builder()
                .submissionId(submission.getId())
                .buyerId(submission.getBuyer().getId())
                .buyerName(submission.getBuyerName())
                .buyerContact(submission.getBuyerContact())
                .quantity(submission.getQuantity())
                .paymentStatus(submission.getPaymentStatus())
                .submittedAt(submission.getCreatedAt())
                .build();
    }


    public static SubmissionResDTO.SubmissionDetailDTO toSubmissionDetailDTO(Submission submission) {
        Form form = submission.getForm();

        return SubmissionResDTO.SubmissionDetailDTO.builder()
                .submissionId(submission.getId())
                .formId(form.getId())
                .formTitle(form.getTitle())
                .formImageUrl(form.getImageUrl())
                .pricePerUnit(form.getPricePerUnit())
                .buyerId(submission.getBuyer().getId())
                .buyerName(submission.getBuyerName())
                .buyerContact(submission.getBuyerContact())
                .quantity(submission.getQuantity())
                .paymentStatus(submission.getPaymentStatus())
                .submittedAt(submission.getCreatedAt())
                .accountBank(form.getAccountBank())
                .accountNumber(form.getAccountNumber())
                .accountName(form.getAccountName())
                .build();
    }
}