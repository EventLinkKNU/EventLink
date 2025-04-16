package com.springboot.eventlink.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.eventlink.chat.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {
    private final ObjectMapper mapper;
    private final Map<WebSocketSession, Long> sessionUserMap = new HashMap<>();

    // 현재 연결된 세션들
    private final Set<WebSocketSession> sessions = new HashSet<>();

    // 방 번호 -> 세션 목록
    private final Map<Long, Set<WebSocketSession>> chatRoomSessionMap = new HashMap<>();

    public void connect(WebSocketSession session) {
        log.info("{} 연결됨", session.getId());
        sessions.add(session);
    }

    public void disconnect(WebSocketSession session) {
        log.info("{} 연결 끊김", session.getId());
        sessions.remove(session);

        chatRoomSessionMap.forEach((roomId, sessionSet) -> sessionSet.remove(session));
    }


    public void handleMessage(WebSocketSession session, String payload) throws IOException {
        ChatMessageDto chatMessageDto = mapper.readValue(payload, ChatMessageDto.class);
        Long chatRoomId = chatMessageDto.getChatRoomId();

        chatRoomSessionMap.computeIfAbsent(chatRoomId, k -> new HashSet<>());
        Set<WebSocketSession> chatRoomSession = chatRoomSessionMap.get(chatRoomId);

        if (chatMessageDto.getMessageType().equals(ChatMessageDto.MessageType.ENTER)) {
            chatRoomSession.add(session);
            sessionUserMap.put(session, chatMessageDto.getSenderId());
        }

        if (chatRoomSession.size() >= 3) {
            removeClosedSession(chatRoomSession);
        }

        sendMessageToChatRoom(chatMessageDto, chatRoomSession);
    }

    private void removeClosedSession(Set<WebSocketSession> chatRoomSession) {
        chatRoomSession.removeIf(sess -> !sessions.contains(sess));
    }

    private void sendMessageToChatRoom(ChatMessageDto chatMessageDto, Set<WebSocketSession> chatRoomSession) {
        chatRoomSession.parallelStream()
                .filter(sess -> {
                    Long senderId = sessionUserMap.get(sess);
                    return senderId == null || !senderId.equals(chatMessageDto.getSenderId());
                })
                .forEach(sess -> sendMessage(sess, chatMessageDto));
    }



    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error("메시지 전송 실패", e);
        }
    }
}
