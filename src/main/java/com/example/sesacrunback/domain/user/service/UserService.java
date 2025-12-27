package com.example.sesacrunback.domain.user.service;

import com.example.sesacrunback.domain.user.dto.request.LoginReqDto;
import com.example.sesacrunback.domain.user.dto.request.SignUpReqDto;
import com.example.sesacrunback.domain.user.dto.response.LoginResDto;
import com.example.sesacrunback.domain.user.dto.response.UserResDto;
import com.example.sesacrunback.domain.user.entity.User;
import com.example.sesacrunback.domain.user.repository.UserRepository;
import com.example.sesacrunback.global.exception.CustomException;
import com.example.sesacrunback.global.exception.ErrorCode;
import com.example.sesacrunback.global.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;


    @Transactional
    public UserResDto signUp(SignUpReqDto signUpReqDto) {
        // 사용자 중복된 이메일 검증
        if(userRepository.existsUserByEmail(signUpReqDto.getEmail())){
            throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        // 비밀번호 암호화
        //passwordEncoder 작업을 Service에서 진행해야 할지 고민중..

        // DTO를 UserEntity로 변환
        User user = SignUpReqDto.toEntity(signUpReqDto,passwordEncoder.encode(signUpReqDto.getPassword()));


        User saved = userRepository.save(user);


        return UserResDto.from(saved);
    }

    public LoginResDto login(LoginReqDto loginReqDto) {

        // 입력한 이메일이 데이터 베이스에 존재하는지 여부
        User user = userRepository.findByEmail(loginReqDto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 입력한 비밀번호가 불러온 user의 password와 일치하는지여부
        // 시큐리티 도입이후 변경상황을 고려해서 코드 작성 필요
        if (!passwordEncoder.matches(loginReqDto.getPassword(), user.getPassword())) {
            throw new CustomException(ErrorCode.LOGIN_FAILED);
        }

        // 해당 유저의 토큰 발급
        String accessToken = jwtProvider.createAccessToken(user.getEmail(), user.getRole());
        String refreshToken = jwtProvider.createRefreshToken(user.getEmail());

        return LoginResDto.of(accessToken, refreshToken);

    }
    
    
}
