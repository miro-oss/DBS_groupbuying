package com.example.groupbuying.domain.groupbuy.service.command;

import com.example.groupbuying.domain.groupbuy.converter.SubmissionConverter;
import com.example.groupbuying.domain.groupbuy.dto.req.SubmissionReqDTO;
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
import com.example.groupbuying.domain.users.entity.User;
import com.example.groupbuying.domain.users.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SubmissionCommandServiceImpl implements SubmissionCommandService {

    private final SubmissionRepository submissionRepository;
    private final FormRepository formRepository;
    private final UsersRepository usersRepository;

    @Override
    public SubmissionResDTO.CreateSubmissionResultDTO createSubmission(
            Long formId,
            Long buyerId,
            SubmissionReqDTO.CreateSubmissionDTO request
    ) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new FormException(FormErrorCode.FORM_NOT_FOUND));

        User buyer = usersRepository.findById(buyerId)
                .orElseThrow(() -> new SubmissionException(SubmissionErrorCode.SUBMISSION404_2));

        if (submissionRepository.existsByFormIdAndBuyerId(formId, buyerId)) {
            throw new SubmissionException(SubmissionErrorCode.SUBMISSION409_1);
        }

        Submission submission = SubmissionConverter.toSubmissionEntity(form, buyer, request);

        Submission saved = submissionRepository.save(submission);

        return SubmissionConverter.toCreateSubmissionResultDTO(saved);
    }

    @Override
    public void updateSubmissionStatus(
            Long sellerId,
            Long formId,
            Long submissionId,
            PaymentStatus status
    ) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new FormException(FormErrorCode.FORM_NOT_FOUND));

        if (!form.getSeller().getId().equals(sellerId)) {
            throw new FormException(FormErrorCode.FORM_FORBIDDEN);
        }

        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new SubmissionException(SubmissionErrorCode.SUBMISSION404_1));

        if (!submission.getForm().getId().equals(formId)) {
            throw new SubmissionException(SubmissionErrorCode.SUBMISSION403_1);
        }

        submission.updatePaymentStatus(status);
    }

    @Override
    public void bulkUpdateSubmissionStatus(
            Long sellerId,
            Long formId,
            List<Long> submissionIds,
            PaymentStatus status
    ) {
        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new FormException(FormErrorCode.FORM_NOT_FOUND));

        if (!form.getSeller().getId().equals(sellerId)) {
            throw new FormException(FormErrorCode.FORM_FORBIDDEN);
        }

        List<Submission> submissions =
                submissionRepository.findByIdInAndFormId(submissionIds, formId);

        submissions.forEach(s -> s.updatePaymentStatus(status));
    }

    @Override
    public void updateSubmissionInfoByBuyer(
            Long buyerId,
            Long submissionId,
            SubmissionReqDTO.UpdateSubmissionInfoDTO request
    ){
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new SubmissionException(SubmissionErrorCode.SUBMISSION404_1));

        if(!submission.getBuyer().getId().equals(buyerId)) {
            throw new SubmissionException(SubmissionErrorCode.SUBMISSION403_1);
        }
        if(submission.getPaymentStatus() != PaymentStatus.WAITING) {
            throw new SubmissionException(SubmissionErrorCode.SUBMISSION409_2);
        }
        submission.updateBuyerInfo(
                request.buyerName(),
                request.buyerContact(),
                request.quantity()
        );
    }
}