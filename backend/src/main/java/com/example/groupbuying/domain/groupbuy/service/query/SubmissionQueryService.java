package com.example.groupbuying.domain.groupbuy.service.query;

import com.example.groupbuying.domain.groupbuy.dto.res.SubmissionResDTO;

import java.util.List;

public interface SubmissionQueryService {

    List<SubmissionResDTO.MySubmissionSummaryDTO> getMySubmissions(Long buyerId);

    List<SubmissionResDTO.FormSubmissionDTO> getFormSubmissionsForSeller(Long sellerId, Long formId);

    SubmissionResDTO.SubmissionDetailDTO getMySubmissionDetail(Long buyerId, Long submissionId);

    SubmissionResDTO.FormStatsDTO getFormStatsForSeller(Long sellerId, Long formId);
}