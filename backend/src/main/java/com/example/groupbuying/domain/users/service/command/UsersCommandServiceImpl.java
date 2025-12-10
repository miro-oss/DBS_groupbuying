package com.example.groupbuying.domain.users.service.command;

import com.example.groupbuying.domain.groupbuy.entity.Form;
import com.example.groupbuying.domain.groupbuy.entity.Submission;
import com.example.groupbuying.domain.groupbuy.repository.FormRepository;
import com.example.groupbuying.domain.groupbuy.repository.SubmissionRepository;
import com.example.groupbuying.domain.users.converter.UsersConverter;
import com.example.groupbuying.domain.users.dto.req.UsersReqDTO;
import com.example.groupbuying.domain.users.dto.res.UsersResDTO;
import com.example.groupbuying.domain.users.entity.User;
import com.example.groupbuying.domain.users.exception.UsersException;
import com.example.groupbuying.domain.users.exception.code.UsersErrorCode;
import com.example.groupbuying.domain.users.repository.UsersRepository;
import com.example.groupbuying.global.jwt.JwtTokenProvider;
import com.example.groupbuying.global.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UsersCommandServiceImpl implements UsersCommandService {

    private final UsersRepository usersRepository;
    private final FormRepository formRepository;
    private final SubmissionRepository submissionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public UsersResDTO.SignUpResultDTO signUp(UsersReqDTO.SignUpDTO request) {
        if (usersRepository.existsByEmail(request.getEmail())) {
            throw new UsersException(UsersErrorCode.DUPLICATE_EMAIL);
        }
        User newUser = UsersConverter.toUser(request);
        newUser.encodePassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = usersRepository.save(newUser);
        return UsersConverter.toSignUpResultDTO(savedUser);
    }

    @Override
    public UsersResDTO.LoginResultDTO login(UsersReqDTO.LoginDTO request) {
        User user = usersRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsersException(UsersErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UsersException(UsersErrorCode.INVALID_CREDENTIALS);
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getEmail(), user.getNickname());
        return UsersConverter.toLoginResultDTO(user, accessToken);
    }

    @Override
    public void updateProfile(UsersReqDTO.UpdateProfileDTO request) {
        Long userId = SecurityUtil.getCurrentUserId();
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new UsersException(UsersErrorCode.USER_NOT_FOUND));

        user.updateProfile(request.getNickname(), request.getPhone());
    }

    @Override
    public void deleteUser() {
        Long userId = SecurityUtil.getCurrentUserId();
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new UsersException(UsersErrorCode.USER_NOT_FOUND));

        List<Submission> mySubmissions = submissionRepository.findByBuyerIdOrderByCreatedAtDesc(userId);

        boolean hasActiveOrders = mySubmissions.stream()
                .anyMatch(s -> s.getPaymentStatus() != com.example.groupbuying.domain.groupbuy.enums.PaymentStatus.COMPLETED
                        && s.getPaymentStatus() != com.example.groupbuying.domain.groupbuy.enums.PaymentStatus.CANCELED);

        if (hasActiveOrders) {
            throw new UsersException(UsersErrorCode.CANNOT_LEAVE_WITH_ACTIVE_ORDERS);
        }

        List<Form> myForms = formRepository.findBySeller(user);
        for (Form form : myForms) {
            List<Submission> formSubmissions = submissionRepository.findByFormIdOrderByCreatedAtAsc(form.getId());
            if (!formSubmissions.isEmpty()) {
                submissionRepository.deleteAll(formSubmissions);
            }
            formRepository.delete(form);
        }
        if (!mySubmissions.isEmpty()) {
            submissionRepository.deleteAll(mySubmissions);
        }
        usersRepository.delete(user);
    }
}