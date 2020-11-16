package com.websocket.board.config;

import com.websocket.board.config.handler.StompHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@RequiredArgsConstructor
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/sub");
        config.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
//         registry.addEndpoint("/api/board/ws-stomp").setAllowedOrigins("*") //로컬용
        registry.addEndpoint("/ws-stomp").setAllowedOrigins("*") // 서버용
                .withSockJS(); // sock.js를 통하여 낮은 버전의 브라우저에서도 websocket이 동작할수 있게 합니다.
    }

    @Override
    // 웹 소켓 클라이언트로부터 들어오는 메시지에 대한 채널을 설정해주는 -> 핸들러를 통해 분기
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }
}
