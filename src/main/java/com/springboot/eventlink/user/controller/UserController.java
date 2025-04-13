package com.springboot.eventlink.user.controller;

import com.springboot.eventlink.user.dto.CustomOAuth2User;
import com.springboot.eventlink.user.dto.UserDTO;
import com.springboot.eventlink.user.dto.UserUpdateRequest;
import com.springboot.eventlink.user.entity.Users;
import com.springboot.eventlink.user.repository.UserRepository;
import com.springboot.eventlink.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
public class UserController {

    private static final Logger logger = Logger.getLogger(UserController.class.getName());

    private final UserRepository userRepository;
    private final UserService userService;

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/api/user/profile")
    public ResponseEntity<UserDTO> getUser(@AuthenticationPrincipal CustomOAuth2User principal) {
        UserDTO user = userService.getUserInfo(principal.getUsername());
        return ResponseEntity.ok(user);
    }

    @PutMapping("/api/user")
    public ResponseEntity<?> updateUser(
            @AuthenticationPrincipal CustomOAuth2User principal,
            @RequestBody UserUpdateRequest request) {

        userService.updateUser(principal.getUsername(), request);
        return ResponseEntity.ok("수정 완료");
    }

    @DeleteMapping("/api/user/delete")
    public ResponseEntity<String> withdraw(Authentication authentication) {
        if (!(authentication instanceof OAuth2AuthenticationToken authToken)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("인증 정보가 없습니다.");
        }

        OAuth2User oAuth2User = authToken.getPrincipal();
        String email = (String) oAuth2User.getAttributes().get("email");

        Optional<Users> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        }

        userRepository.delete(userOptional.get());

        return ResponseEntity.ok("탈퇴 완료");
    }
}
