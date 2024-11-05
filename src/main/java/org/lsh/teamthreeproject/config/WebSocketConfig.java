package org.lsh.teamthreeproject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 클라이언트가 구독할 수 있는 주소(prefix) 설정
        config.enableSimpleBroker("/topic");  // 메시지를 브로드캐스트할 경로 (브로커 역할)
        config.setApplicationDestinationPrefixes("/app");  // 클라이언트가 메시지를 보낼 때 사용할 경로의 prefix
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트가 소켓을 연결할 수 있는 엔드포인트 설정
        registry.addEndpoint("/ws")  // "/ws" 엔드포인트로 소켓 연결
                .setAllowedOriginPatterns("*") //모든 도메인을 허용
                .withSockJS();  // SockJS 사용
    }
}
