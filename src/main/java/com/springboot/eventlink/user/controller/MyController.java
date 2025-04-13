package com.springboot.eventlink.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MyController {

    @GetMapping("/my")
    @ResponseBody
    public String myAPI() {

        return "my route";
    }

    @GetMapping("/myjwt")
    public ResponseEntity<String> mypage() {

        return ResponseEntity.ok("인증된 사용자만 볼 수 있는 페이지입니다!");
    }

    @GetMapping("/loginpage")
    public String loginpage() {
        return "redirect:http://localhost:3000";  // React 애플리케이션이 실행 중인 주소로 리다이렉트
    }

}
