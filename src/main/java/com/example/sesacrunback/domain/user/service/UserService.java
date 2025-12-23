package com.example.sesacrunback.domain.user.service;

import com.example.sesacrunback.domain.user.dto.request.UserCreateReqDto;
import com.example.sesacrunback.domain.user.dto.response.UserResDto;
import com.example.sesacrunback.domain.user.entity.User;
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
        // 사용자 중복된 이메일 검증
        if(userRepository.existsUserByEmail(createReqDto.getEmail())){
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        // DTO를 UserEntity로 변환
        User user = User.from(createReqDto);

        // 비밀번호 암호화
        // 비밀번호 암호화 부분은 security를 넣으면서 추가 예정

        User saved = userRepository.save(user);

        return UserResDto.from(saved);
    }
}
