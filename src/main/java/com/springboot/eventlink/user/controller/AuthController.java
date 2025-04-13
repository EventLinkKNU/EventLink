package com.springboot.eventlink.user.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {

        // Authorization 쿠키 삭제
        ResponseCookie cookie = ResponseCookie.from("Authorization", "")
                .path("/")
                .httpOnly(true)
                .maxAge(0) // 쿠키 즉시 만료
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok("로그아웃 완료");
    }
}
