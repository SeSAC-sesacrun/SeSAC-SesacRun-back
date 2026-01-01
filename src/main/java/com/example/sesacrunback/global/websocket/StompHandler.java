package com.example.sesacrunback.global.websocket;

import com.example.sesacrunback.global.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        // 클라이언트가 보낸 메시지 헤더를 STOMP 형태 접근자로 변환
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message,
            StompHeaderAccessor.class);

        // 연결 시도인 경우에만 토큰 검증
        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {

            String token = resolveToken(accessor);

            if (jwtProvider.validateToken(token)) {
                String email = jwtProvider.getEmailFromToken(token);

                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null,
                    userDetails.getAuthorities());

                accessor.setUser(authentication);
            }
        }
        return message;
    }

    private String resolveToken(StompHeaderAccessor accessor) {
        String rawToken = accessor.getFirstNativeHeader("Authorization");

        if (rawToken != null && rawToken.startsWith("Bearer ")) {
            return rawToken.substring(7);
        }
        return null;
    }


}
