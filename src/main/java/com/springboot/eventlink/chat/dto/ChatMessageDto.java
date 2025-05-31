package com.springboot.eventlink.chat.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto {
    // 메세지 타입 : 입장, 채팅
    public enum MessageType {
        ENTER, TALK;
    }

    private Long roomId;
    private MessageType messageType; // 메시지 타입
    private Long chatId;
    private Long sendId;  // 채팅을 보낸 사람
    private Long receiveId; //채팅을 받는 사람
    private String message; // 메시지
    private String createdAt; // 채팅 발송 시간
}
