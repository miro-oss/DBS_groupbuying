package com.example.groupbuying.domain.groupbuy.service.command;

import com.example.groupbuying.domain.groupbuy.dto.req.SubmissionReqDTO;
import com.example.groupbuying.domain.groupbuy.dto.res.SubmissionResDTO;
import com.example.groupbuying.domain.groupbuy.enums.PaymentStatus;

import java.util.List;

public interface SubmissionCommandService {

    SubmissionResDTO.CreateSubmissionResultDTO createSubmission(
            Long formId,
            Long buyerId,
            SubmissionReqDTO.CreateSubmissionDTO request
    );

    void updateSubmissionStatus(
            Long sellerId,
            Long formId,
            Long submissionId,
            PaymentStatus status
    );

    void bulkUpdateSubmissionStatus(
            Long sellerId,
            Long formId,
            List<Long> submissionIds,
            PaymentStatus status
    );

    void updateSubmissionInfoByBuyer(
            Long buyerId,
            Long submissionId,
            SubmissionReqDTO.UpdateSubmissionInfoDTO request
    );

    void confirmTransaction(Long buyerId, Long submissionId);
}