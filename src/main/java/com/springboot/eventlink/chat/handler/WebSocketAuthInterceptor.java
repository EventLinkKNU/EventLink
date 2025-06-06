package com.springboot.eventlink.chat.handler;

import com.springboot.eventlink.user.jwt.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class WebSocketAuthInterceptor implements HandshakeInterceptor {

    private final JWTUtil jwtUtil;

    public WebSocketAuthInterceptor(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

//    @Override
//    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
//                                   Map<String, Object> attributes) {
//
//        if (request instanceof ServletServerHttpRequest servletRequest) {
//            HttpServletRequest httpRequest = servletRequest.getServletRequest();
//
//            String token = httpRequest.getParameter("token");
//            if (token == null || jwtUtil.isExpired(token)) {
//                return false;
//            }
//
//            String username = jwtUtil.getUsername(token);
//            attributes.put("username", username); // WebSocketSession에 저장
//            return true;
//        }
//        return false;
//    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {

        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest httpRequest = servletRequest.getServletRequest();

            String query = httpRequest.getQueryString();
            String token = null;

            if (query != null && query.contains("token=")) {
                token = query.replaceAll(".*token=", "").split("&")[0];
            }

            System.out.println("🔥 WebSocketAuthInterceptor - token: " + token);

            // ✅ 여기서 서버에 들어온 token 의 exp 체크를 로그로 남기자
            try {
                boolean expired = jwtUtil.isExpired(token);
                System.out.println("✅ Token Expiration Check → expired: " + expired);
                if (expired) {
                    System.out.println("❌ token expired → handshake reject");
                    return false;
                }
            } catch (Exception e) {
                System.out.println("❌ Token parse error in isExpired: " + e.getMessage());
                return false;
            }

            String username = jwtUtil.getUsername(token);
            attributes.put("username", username); // WebSocketSession 에 저장
            System.out.println("✅ handshake success - user: " + username);

            return true;
        }

        return false;
    }



    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {}
}