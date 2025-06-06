package com.springboot.eventlink.chat.config;

import com.springboot.eventlink.chat.handler.WebSocketAuthInterceptor;
import com.springboot.eventlink.chat.handler.WebSocketChatHandler;
import com.springboot.eventlink.user.jwt.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final WebSocketChatHandler webSocketChatHandler;
    private final JWTUtil jwtUtil;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(webSocketChatHandler, "/ws/chat").addInterceptors(new WebSocketAuthInterceptor(jwtUtil)).setAllowedOrigins("*");
        registry.addHandler(webSocketChatHandler, "/ws/chat")
                .addInterceptors(new WebSocketAuthInterceptor(jwtUtil))
                .setAllowedOriginPatterns("http://localhost:3000");

    }
}
