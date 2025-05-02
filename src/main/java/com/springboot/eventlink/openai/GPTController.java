package com.springboot.eventlink.openai;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/gpt")
@RequiredArgsConstructor
public class GPTController {

    private final GPTService gptService;

    @PostMapping("/review")
    public Map<String, String> review(@RequestBody RequestContentDto request) {
        String content = request.getContent();
        boolean approved = gptService.isEventApproved(content);
        return Map.of("status", approved ? "통과" : "비통과");
    }
}

