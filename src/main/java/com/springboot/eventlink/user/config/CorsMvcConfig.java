package com.springboot.eventlink.user.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")  // 모든 요청에 대해 CORS 설정
                .allowedOrigins("http://localhost:3000", "ws://localhost:8080/ws/chat")  // React 앱의 URL
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // 허용할 HTTP 메서드
                .allowedHeaders("*")  // 모든 헤더 허용
                .allowCredentials(true)  // 쿠키 허용
                .exposedHeaders("Set-Cookie", "Authorization");  // 클라이언트에서 볼 수 있도록 노출할 헤더
    }
}
