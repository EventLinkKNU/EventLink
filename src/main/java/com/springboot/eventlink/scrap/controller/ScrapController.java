package com.springboot.eventlink.scrap.controller;
import com.springboot.eventlink.scrap.dto.ScrapRequestDto;
import com.springboot.eventlink.scrap.dto.ScrapResponseDto;
import com.springboot.eventlink.scrap.entity.Scrap;
import com.springboot.eventlink.scrap.service.ScrapService;
import com.springboot.eventlink.user.dto.CustomOAuth2User;
import com.springboot.eventlink.user.entity.Users;
import com.springboot.eventlink.user.repository.UserRepository;
import com.springboot.eventlink.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scraps")
@RequiredArgsConstructor
public class ScrapController {
    private final UserService userService;
    private final ScrapService scrapService;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<String> toggleScrap(@RequestParam Long eventId, @AuthenticationPrincipal CustomOAuth2User user) {
        String username = user.getUsername();
        Long userId = userRepository.findByUsername(username).getId();

        try {
            boolean isScrapped = scrapService.isScrapped(userId, eventId);

            if (isScrapped) {
                scrapService.deleteScrapByUserAndEvent(userId, eventId);
                return ResponseEntity.ok("스크랩이 취소되었습니다.");
            } else {
                ScrapRequestDto dto = new ScrapRequestDto();
                dto.setEventId(eventId);
                dto.setCreatorId(userId);
                Scrap scrap = scrapService.createScrap(dto);
                return ResponseEntity.ok("스크랩이 완료되었습니다. ID: " + scrap.getId());
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("스크랩 처리 중 오류 발생");
        }
    }

    @GetMapping("/is-scrapped")
    public ResponseEntity<Boolean> isScrapped(@RequestParam Long eventId, @AuthenticationPrincipal CustomOAuth2User user) {
        String username = user.getUsername();
        Long userId = userRepository.findByUsername(username).getId();
        boolean result = scrapService.isScrapped(userId, eventId);
        return ResponseEntity.ok(result);
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
