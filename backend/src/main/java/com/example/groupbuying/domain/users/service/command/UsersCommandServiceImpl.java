package com.example.groupbuying.domain.users.service.command;

import com.example.groupbuying.domain.users.converter.UsersConverter;
import com.example.groupbuying.domain.users.dto.req.UsersReqDTO;
import com.example.groupbuying.domain.users.dto.res.UsersResDTO;
import com.example.groupbuying.domain.users.entity.User;
import com.example.groupbuying.domain.users.exception.UsersException;
import com.example.groupbuying.domain.users.exception.code.UsersErrorCode;
import com.example.groupbuying.domain.users.repository.UsersRepository;
import com.example.groupbuying.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UsersCommandServiceImpl implements UsersCommandService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    private static final String ALLOWED_EMAIL_DOMAIN = "@dgu.ac.kr";

    private void validateEmailDomain(String email) {
        String trimmed = email.trim().toLowerCase();
        if(!trimmed.endsWith(ALLOWED_EMAIL_DOMAIN)) {
            throw new UsersException(UsersErrorCode.INVALID_EMAIL_DOMAIN);
        }
    }

    @Override
    @Transactional
    public UsersResDTO.SignUpResultDTO signUp(UsersReqDTO.SignUpDTO request){
        validateEmailDomain(request.getEmail());

        if(usersRepository.existsByEmail(request.getEmail())) {
            throw new UsersException(UsersErrorCode.DUPLICATE_EMAIL);
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = UsersConverter.toUser(request, encodedPassword);
        User saved = usersRepository.save(user);

        return UsersConverter.toSignUpResultDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public UsersResDTO.LoginResultDTO login(UsersReqDTO.LoginDTO request) {

        User user = usersRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsersException(UsersErrorCode.INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UsersException(UsersErrorCode.INVALID_CREDENTIALS);
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getId(), user.getEmail());

        return UsersConverter.toLoginResultDTO(user, accessToken);
    }

    @Override
    @Transactional
    public UsersResDTO.ProfileDTO updateProfile(Long userId, UsersReqDTO.UpdateProfileDTO request) {

        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new UsersException(UsersErrorCode.USER_NOT_FOUND));


        user.updateProfile(request.getNickname(), request.getPhone());


        return UsersConverter.toProfileDTO(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new UsersException(UsersErrorCode.USER_NOT_FOUND));

        usersRepository.delete(user);
    }
}

