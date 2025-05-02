package com.springboot.eventlink.openai;

// OpenAIResponse.java

import lombok.Data;

import java.util.List;

@Data
public class OpenAIResponse {
    private List<Choice> choices;

    @Data
    public static class Choice {
        private OpenAIRequest.Message message;
    }
}


