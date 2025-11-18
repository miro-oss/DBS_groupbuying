package com.example.groupbuying.domain.groupbuy.service.command;

import com.example.groupbuying.domain.groupbuy.dto.req.FormReqDTO;
import com.example.groupbuying.domain.groupbuy.dto.res.FormResDTO;

public interface FormCommandService {

    FormResDTO.CreateFormResultDTO createForm(Long sellerId, FormReqDTO.CreateFormDTO request);
    FormResDTO.FormDetailDTO updateForm(Long sellerId, Long formId, FormReqDTO.UpdateFormDTO request);

    void closeForm(Long sellerId, Long formId);
}