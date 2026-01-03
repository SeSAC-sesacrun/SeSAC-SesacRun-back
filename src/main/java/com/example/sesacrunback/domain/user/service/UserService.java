package com.example.sesacrunback.domain.user.service;

import com.example.sesacrunback.domain.user.dto.response.UserResDto;
import com.example.sesacrunback.domain.user.entity.User;
import com.example.sesacrunback.domain.user.repository.UserRepository;
import com.example.sesacrunback.global.exception.CustomException;
import com.example.sesacrunback.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public UserResDto getMyProfile(Long id) {
       User user =  userRepository.findById(id).orElseThrow(
            ()-> new CustomException(ErrorCode.MEMBER_NOT_FOUND)
        );

       return UserResDto.from(user);
    }
}
