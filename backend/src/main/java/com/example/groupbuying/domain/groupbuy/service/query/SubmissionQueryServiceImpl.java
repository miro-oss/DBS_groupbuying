package com.example.groupbuying.domain.groupbuy.service.query;

import com.example.groupbuying.domain.groupbuy.converter.SubmissionConverter;
import com.example.groupbuying.domain.groupbuy.dto.res.SubmissionResDTO;
import com.example.groupbuying.domain.groupbuy.entity.Form;
import com.example.groupbuying.domain.groupbuy.entity.Submission;
import com.example.groupbuying.domain.groupbuy.enums.PaymentStatus;
import com.example.groupbuying.domain.groupbuy.exception.FormException;
import com.example.groupbuying.domain.groupbuy.exception.SubmissionException;
import com.example.groupbuying.domain.groupbuy.exception.code.FormErrorCode;
import com.example.groupbuying.domain.groupbuy.exception.code.SubmissionErrorCode;
import com.example.groupbuying.domain.groupbuy.repository.FormRepository;
import com.example.groupbuying.domain.groupbuy.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubmissionQueryServiceImpl implements SubmissionQueryService {

    private final SubmissionRepository submissionRepository;
    private final FormRepository formRepository;

    @Override
    public List<SubmissionResDTO.MySubmissionSummaryDTO> getMySubmissions(Long buyerId) {
        List<Submission> submissions =
                submissionRepository.findByBuyerIdOrderByCreatedAtDesc(buyerId);

        return submissions.stream()
                .map(SubmissionConverter::toMySubmissionSummaryDTO)
                .toList();
    }

    @Override
    public List<SubmissionResDTO.FormSubmissionDTO> getFormSubmissionsForSeller(Long sellerId, Long formId) {

        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new FormException(FormErrorCode.FORM_NOT_FOUND));

        if (!form.getSeller().getId().equals(sellerId)) {
            throw new FormException(FormErrorCode.FORM_FORBIDDEN);
        }

        List<Submission> submissions =
                submissionRepository.findByFormIdOrderByCreatedAtAsc(formId);

        return submissions.stream()
                .map(SubmissionConverter::toFormSubmissionDTO)
                .toList();
    }

    @Override
    public SubmissionResDTO.SubmissionDetailDTO getMySubmissionDetail(Long buyerId, Long submissionId) {
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new SubmissionException(SubmissionErrorCode.SUBMISSION409_1));

        if(!submission.getBuyer().getId().equals(buyerId)) {
            throw new SubmissionException(SubmissionErrorCode.SUBMISSION403_1);
        }

        return SubmissionConverter.toSubmissionDetailDTO(submission);
    }

    @Override
    public SubmissionResDTO.FormStatsDTO getFormStatsForSeller(Long sellerId, Long formId) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new FormException(FormErrorCode.FORM_NOT_FOUND));

        if(!form.getSeller().getId().equals(sellerId)) {
            throw new FormException(FormErrorCode.FORM_FORBIDDEN);
        }

        List<Submission> submissions = submissionRepository.findByFormIdOrderByCreatedAtAsc(formId);

        int totalSubmissions = submissions.size();
        long totalQuantity = submissions.stream()
                .mapToLong(Submission::getQuantity)
                .sum();

        Map<PaymentStatus, IntSummaryStatistics> summaryMap =
                submissions.stream()
                        .collect(Collectors.groupingBy(
                                Submission::getPaymentStatus,
                                Collectors.summarizingInt(Submission::getQuantity)
                        ));

        List<SubmissionResDTO.PaymentStatusStatDTO> statusStats = summaryMap.entrySet().stream()
                .map(e -> SubmissionResDTO.PaymentStatusStatDTO.builder()
                        .status(e.getKey())
                        .count(e.getValue().getCount())
                        .totalQuantity(e.getValue().getSum())
                        .build()
                )
                .toList();

        return SubmissionResDTO.FormStatsDTO.builder()
                .formId(formId)
                .totalSubmissions(totalSubmissions)
                .totalQuantity(totalQuantity)
                .statusStats(statusStats)
                .build();
    }
}