package com.example.groupbuying.domain.groupbuy.service.query;

import com.example.groupbuying.domain.groupbuy.dto.req.FormReqDTO;
import com.example.groupbuying.domain.groupbuy.dto.res.FormResDTO;

import java.util.List;

public interface FormQueryService {

    List<FormResDTO.FormSummaryDTO> getForms(FormReqDTO.SearchDTO search);

    FormResDTO.FormDetailDTO getFormDetail(Long formId);

    List<FormResDTO.FormSummaryDTO> getMyForms(Long userId);
}