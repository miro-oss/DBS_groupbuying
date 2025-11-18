package com.example.groupbuying.domain.groupbuy.service.command;

import com.example.groupbuying.domain.groupbuy.converter.FormConverter;
import com.example.groupbuying.domain.groupbuy.dto.req.FormReqDTO;
import com.example.groupbuying.domain.groupbuy.dto.res.FormResDTO;
import com.example.groupbuying.domain.groupbuy.entity.Category;
import com.example.groupbuying.domain.groupbuy.entity.Form;
import com.example.groupbuying.domain.groupbuy.exception.FormException;
import com.example.groupbuying.domain.groupbuy.exception.code.FormErrorCode;
import com.example.groupbuying.domain.groupbuy.repository.CategoryRepository;
import com.example.groupbuying.domain.groupbuy.repository.FormRepository;
import com.example.groupbuying.domain.users.entity.User;
import com.example.groupbuying.domain.users.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class FormCommandServiceImpl implements FormCommandService {

    private final FormRepository formRepository;
    private final CategoryRepository categoryRepository;
    private final UsersRepository usersRepository;

    @Override
    public FormResDTO.CreateFormResultDTO createForm(Long sellerId,
                                                     FormReqDTO.CreateFormDTO request) {

        User seller = usersRepository.findById(sellerId)
                .orElseThrow(() -> new FormException(FormErrorCode.FORM404_1));

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new FormException(FormErrorCode.FORM404_2));

        Form form = FormConverter.toFormEntity(seller, category, request);

        Form saved = formRepository.save(form);

        return FormConverter.toCreateFormResultDTO(saved);
    }

    @Override
    public FormResDTO.FormDetailDTO updateForm(Long sellerId, Long formId, FormReqDTO.UpdateFormDTO dto) {

        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new FormException(FormErrorCode.FORM_NOT_FOUND));

        if (!form.getSeller().getId().equals(sellerId)) {
            throw new FormException(FormErrorCode.FORM_FORBIDDEN);
        }

        Category newCategory = null;
        if (dto.categoryId() != null) {
            newCategory = categoryRepository.findById(dto.categoryId())
                    .orElseThrow(() -> new FormException(FormErrorCode.FORM404_2));
        }

        form.updateForm(
                dto.title(),
                dto.description(),
                dto.pricePerUnit(),
                dto.imageUrl(),
                dto.orderDate(),
                dto.location(),
                dto.tradeTime(),
                dto.accountBank(),
                dto.accountNumber(),
                dto.accountName(),
                dto.deadline(),
                newCategory
        );

        return FormConverter.toDetailDTO(form);
    }

    @Override
    public void closeForm(Long sellerId, Long formId) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new FormException(FormErrorCode.FORM_NOT_FOUND));

        if (!form.getSeller().getId().equals(sellerId)) {
            throw new FormException(FormErrorCode.FORM_FORBIDDEN);
        }

        form.close();
    }
}