package com.example.groupbuying.domain.users.service.query;

import com.example.groupbuying.domain.users.converter.UsersConverter;
import com.example.groupbuying.domain.users.dto.res.UsersResDTO;
import com.example.groupbuying.domain.users.entity.User;
import com.example.groupbuying.domain.users.repository.UsersRepository;
import com.example.groupbuying.domain.users.exception.code.UsersErrorCode;
import com.example.groupbuying.global.apiPayload.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UsersQueryServiceImpl implements UsersQueryService {

    private final UsersRepository usersRepository;

    @Override
    public UsersResDTO.ProfileDTO getProfile(Long userId) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(UsersErrorCode.USER_NOT_FOUND));

        return UsersConverter.toProfileDTO(user);
    }
}