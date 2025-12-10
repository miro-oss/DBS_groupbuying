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
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class FormCommandServiceImpl implements FormCommandService {

    private final FormRepository formRepository;
    private final CategoryRepository categoryRepository;
    private final UsersRepository usersRepository;

    private void validateDateSequence(LocalDateTime deadline, LocalDateTime orderDate, LocalDateTime tradeTime) {
        if (orderDate.isBefore(deadline)) {
            throw new FormException(FormErrorCode.INVALID_DATE_SEQUENCE);
        }
        if (tradeTime.isBefore(orderDate)) {
            throw new FormException(FormErrorCode.INVALID_DATE_SEQUENCE);
        }
    }

    @Override
    public FormResDTO.CreateFormResultDTO createForm(Long sellerId,
                                                     FormReqDTO.CreateFormDTO request,
                                                     MultipartFile image) {

        User seller = usersRepository.findById(sellerId)
                .orElseThrow(() -> new FormException(FormErrorCode.FORM404_1));

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new FormException(FormErrorCode.FORM404_2));

        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            try {
                String uploadDir = System.getProperty("user.dir") + "/uploads";
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();

                String originalFilename = image.getOriginalFilename();
                String saveFileName = UUID.randomUUID() + "_" + originalFilename;

                File saveFile = new File(uploadDir, saveFileName);
                image.transferTo(saveFile);

                imageUrl = "http://localhost:8080/images/" + saveFileName;

            } catch (IOException e) {
                throw new RuntimeException("파일 업로드 실패", e);
            }
        }

        String finalImageUrl = (imageUrl != null) ? imageUrl : request.imageUrl();

        Form form = Form.builder()
                .seller(seller)
                .category(category)
                .title(request.title())
                .description(request.description())
                .pricePerUnit(request.pricePerUnit())
                .imageUrl(finalImageUrl)
                .orderDate(request.orderDate())
                .location(request.location())
                .tradeTime(request.tradeTime())
                .accountBank(request.accountBank())
                .accountNumber(request.accountNumber())
                .accountName(request.accountName())
                .deadline(request.deadline())
                .build();

        Form saved = formRepository.save(form);

        return FormConverter.toCreateFormResultDTO(saved);
    }

    @Override
    public FormResDTO.FormDetailDTO updateForm(Long sellerId, Long formId, FormReqDTO.UpdateFormDTO dto, MultipartFile image) {

        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new FormException(FormErrorCode.FORM_NOT_FOUND));

        if (!form.getSeller().getId().equals(sellerId)) {
            throw new FormException(FormErrorCode.FORM_FORBIDDEN);
        }

        String newImageUrl = null;
        if (image != null && !image.isEmpty()) {
            try {
                String uploadDir = System.getProperty("user.dir") + "/uploads";
                File dir = new File(uploadDir);
                if (!dir.exists()) dir.mkdirs();

                String originalFilename = image.getOriginalFilename();
                String saveFileName = UUID.randomUUID() + "_" + originalFilename;

                File saveFile = new File(uploadDir, saveFileName);
                image.transferTo(saveFile);

                newImageUrl = "http://localhost:8080/images/" + saveFileName;

            } catch (IOException e) {
                throw new RuntimeException("파일 업로드 실패", e);
            }
        }

        LocalDateTime newDeadline = (dto.deadline() != null) ? dto.deadline() : form.getDeadline();
        LocalDateTime newOrderDate = (dto.orderDate() != null) ? dto.orderDate() : form.getOrderDate();
        LocalDateTime newTradeTime = (dto.tradeTime() != null) ? dto.tradeTime() : form.getTradeTime();

        validateDateSequence(newDeadline, newOrderDate, newTradeTime);

        if (form.getStatus() == com.example.groupbuying.domain.groupbuy.enums.FormStatus.CLOSED
                && newDeadline.isAfter(LocalDateTime.now())) {
            form.reopen();
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
                newImageUrl,
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

    @Override
    public void deleteForm(Long sellerId, Long formId) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new FormException(FormErrorCode.FORM_NOT_FOUND));

        if(!form.getSeller().getId().equals(sellerId)) {
            throw new FormException(FormErrorCode.FORM_FORBIDDEN);
        }

        formRepository.delete(form);
    }
}