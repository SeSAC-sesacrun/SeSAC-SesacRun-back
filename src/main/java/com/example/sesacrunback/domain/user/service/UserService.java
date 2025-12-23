package com.example.sesacrunback.domain.user.service;

import com.example.sesacrunback.domain.user.dto.request.UserCreateReqDto;
import com.example.sesacrunback.domain.user.dto.response.UserResDto;
import com.example.sesacrunback.domain.user.repository.UserRepository;
import com.example.sesacrunback.global.exception.CustomException;
import com.example.sesacrunback.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResDto signUp(UserCreateReqDto createReqDto) {
        //사용자 중복된 이메일 검증
        if(!userRepository.existsUserByEmail(createReqDto.getEmail())){
            throw new CustomException(ErrorCode.)
        }


    }
}
