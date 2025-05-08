package com.springboot.eventlink.scrap.controller;
import com.springboot.eventlink.scrap.dto.ScrapRequestDto;
import com.springboot.eventlink.scrap.dto.ScrapResponseDto;
import com.springboot.eventlink.scrap.entity.Scrap;
import com.springboot.eventlink.scrap.service.ScrapService;
import com.springboot.eventlink.user.dto.CustomOAuth2User;
import com.springboot.eventlink.user.entity.Users;
import com.springboot.eventlink.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scraps")
@RequiredArgsConstructor
public class ScrapController {

    private final ScrapService scrapService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<String> createScrap(@RequestBody ScrapRequestDto dto) {
        Scrap scrap = scrapService.createScrap(dto);
        return ResponseEntity.ok("스크랩이 완료되었습니다. ID: " + scrap.getId());
    }

    @GetMapping("/myscrap")
    public ResponseEntity<List<ScrapResponseDto>> getMyScrap(@AuthenticationPrincipal CustomOAuth2User user) {
        if (user == null) {
            throw new RuntimeException("로그인된 사용자 정보가 없습니다.");
        }

        String username = user.getUsername();
        Users byUsername = userRepository.findByUsername(username);
        String email = byUsername.getEmail();
        List<ScrapResponseDto> scraps = scrapService.getUserScraps(email);

        return ResponseEntity.ok(scraps);
    }

    @DeleteMapping("/deleteScrap/{scrapId}")
    public ResponseEntity<String> deleteScrap(@PathVariable Long scrapId, @AuthenticationPrincipal CustomOAuth2User user){
        String username = user.getUsername();
        Users byUsername = userRepository.findByUsername(username);
        String email = byUsername.getEmail();

        scrapService.deleteScrap(scrapId, email);

        return ResponseEntity.ok("스크랩취소성공");
    }

}
