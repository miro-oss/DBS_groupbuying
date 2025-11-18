package com.example.groupbuying.domain.groupbuy.service.query;

import com.example.groupbuying.domain.groupbuy.converter.FormConverter;
import com.example.groupbuying.domain.groupbuy.dto.req.FormReqDTO;
import com.example.groupbuying.domain.groupbuy.dto.res.FormResDTO;
import com.example.groupbuying.domain.groupbuy.entity.Form;
import com.example.groupbuying.domain.groupbuy.enums.FormStatus;
import com.example.groupbuying.domain.groupbuy.exception.FormException;
import com.example.groupbuying.domain.groupbuy.exception.code.FormErrorCode;
import com.example.groupbuying.domain.groupbuy.repository.FormRepository;
import com.example.groupbuying.domain.users.entity.User;
import com.example.groupbuying.domain.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FormQueryServiceImpl implements FormQueryService {

    private final FormRepository formRepository;
    private final UsersRepository usersRepository;

    @Override
    public List<FormResDTO.FormSummaryDTO> getForms(FormReqDTO.SearchDTO search) {

        List<Form> forms = formRepository.findAll();

        if (search.categoryId() != null) {
            forms = forms.stream()
                    .filter(f -> f.getCategory().getId().equals(search.categoryId()))
                    .toList();
        }

        if (search.status() != null) {
            FormStatus status = FormStatus.valueOf(search.status().toUpperCase());
            forms = forms.stream()
                    .filter(f -> f.getStatus() == status)
                    .toList();
        }

        if (search.keyword() != null && !search.keyword().isBlank()) {
            forms = forms.stream()
                    .filter(f -> f.getTitle().contains(search.keyword()))
                    .toList();
        }

        return forms.stream()
                .map(FormConverter::toSummaryDTO)
                .toList();
    }

    @Override
    public FormResDTO.FormDetailDTO getFormDetail(Long formId) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new FormException(FormErrorCode.FORM_NOT_FOUND));

        return FormConverter.toDetailDTO(form);
    }

    @Override
    public List<FormResDTO.FormSummaryDTO> getMyForms(Long userId) {

        User seller = usersRepository.findById(userId)
                .orElseThrow(() -> new FormException(FormErrorCode.FORM404_1));

        return formRepository.findBySeller(seller)
                .stream()
                .map(FormConverter::toSummaryDTO)
                .toList();
    }
}