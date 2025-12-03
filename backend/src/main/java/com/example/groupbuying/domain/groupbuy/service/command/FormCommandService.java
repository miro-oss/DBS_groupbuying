package com.example.groupbuying.domain.groupbuy.service.command;

import com.example.groupbuying.domain.groupbuy.dto.req.FormReqDTO;
import com.example.groupbuying.domain.groupbuy.dto.res.FormResDTO;
import org.springframework.web.multipart.MultipartFile;

public interface FormCommandService {

    FormResDTO.CreateFormResultDTO createForm(Long sellerId, FormReqDTO.CreateFormDTO request, MultipartFile image);
    FormResDTO.FormDetailDTO updateForm(Long sellerId, Long formId, FormReqDTO.UpdateFormDTO request);

    void closeForm(Long sellerId, Long formId);
    void deleteForm(Long sellerId, Long formId);
}