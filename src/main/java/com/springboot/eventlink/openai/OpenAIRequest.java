package com.springboot.eventlink.openai;

// OpenAIRequest.java

import lombok.Data;

import java.util.List;

@Data
public class OpenAIRequest {
    private String model;
    private List<Message> messages;

    @Data
    public static class Message {
        private String role;
        private String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}
