package com.example.groupbuying.domain.users.service.query;

import com.example.groupbuying.domain.users.dto.res.UsersResDTO;
import com.example.groupbuying.domain.users.dto.res.UsersResDTO;

public interface UsersQueryService {

    UsersResDTO.ProfileDTO getProfile(Long userId);
}