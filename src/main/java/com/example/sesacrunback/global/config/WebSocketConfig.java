package com.example.sesacrunback.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // STOMP 활성화
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp") // 웹소켓 연결 주소
            .setAllowedOriginPatterns("*"); // 어디든 허용
//            .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 구독 prefix
        // 클라이언트가 메시지를 받을 때
        registry.enableSimpleBroker("/sub", "/queue");

        // 발행 prefix
        // 클라이언트가 서버로 메시지를 보낼 때
        registry.setApplicationDestinationPrefixes("/pub");
    }
}
