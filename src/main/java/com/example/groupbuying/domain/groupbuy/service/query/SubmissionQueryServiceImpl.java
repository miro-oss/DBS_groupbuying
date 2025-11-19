package com.example.groupbuying.domain.groupbuy.service.query;

import com.example.groupbuying.domain.groupbuy.converter.SubmissionConverter;
import com.example.groupbuying.domain.groupbuy.dto.res.SubmissionResDTO;
import com.example.groupbuying.domain.groupbuy.entity.Form;
import com.example.groupbuying.domain.groupbuy.entity.Submission;
import com.example.groupbuying.domain.groupbuy.exception.FormException;
import com.example.groupbuying.domain.groupbuy.exception.code.FormErrorCode;
import com.example.groupbuying.domain.groupbuy.repository.FormRepository;
import com.example.groupbuying.domain.groupbuy.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
}