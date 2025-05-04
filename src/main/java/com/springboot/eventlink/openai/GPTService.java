package com.springboot.eventlink.openai;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GPTService {

    @Value("${openai.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public boolean isEventApproved(String eventDescription) {
        String url = "https://api.openai.com/v1/chat/completions";

        OpenAIRequest request = new OpenAIRequest();
        request.setModel("gpt-3.5-turbo");
        request.setMessages(List.of(
                new OpenAIRequest.Message("system",
                        "당신은 엄격한 마케팅 검열관입니다. 사용자가 입력한 이벤트 소개글을 보고 아래 기준에 따라 검열하세요:\n" +
                                "- 성적으로 불순한 의도를 가지진 않았는지\n" +
                                "- 불쾌하거나 민감한 내용이 없는지\n" +
                                "- 위험한 행위, 장소를 언급하는지\n\n" +
                                "문제없으면 '통과'라고만 대답하세요. 문제가 있다면 '비통과'라고만 대답하세요.\n" +
                                "그 외 다른 말은 하지 마세요."),
                new OpenAIRequest.Message("user", eventDescription)
        ));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<OpenAIRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<OpenAIResponse> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, OpenAIResponse.class);

        String result = response.getBody()
                .getChoices()
                .get(0)
                .getMessage()
                .getContent()
                .trim()
                .toLowerCase();

        return result.startsWith("통과");
    }
}


