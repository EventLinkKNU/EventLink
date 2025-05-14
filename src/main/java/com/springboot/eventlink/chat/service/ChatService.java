package com.springboot.eventlink.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.eventlink.chat.dto.ChatMessageDto;
import com.springboot.eventlink.chat.entity.Chat;
import com.springboot.eventlink.chat.repository.ChatRepository;
import com.springboot.eventlink.user.entity.Users;
import com.springboot.eventlink.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final ObjectMapper mapper;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    private final Map<WebSocketSession, Long> sessionUserMap = new HashMap<>();
    private final Set<WebSocketSession> sessions = new HashSet<>();
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
        ChatMessageDto dto = mapper.readValue(payload, ChatMessageDto.class);
        Long chatRoomId = dto.getChatId();

        chatRoomSessionMap.computeIfAbsent(chatRoomId, k -> new HashSet<>());
        Set<WebSocketSession> chatRoomSession = chatRoomSessionMap.get(chatRoomId);

        if (dto.getMessageType() == ChatMessageDto.MessageType.ENTER) {
            chatRoomSession.add(session);
            sessionUserMap.put(session, dto.getSendId());
        }

        if (dto.getMessageType() == ChatMessageDto.MessageType.TALK) {
            Chat chat = new Chat();
            chat.setMessage(dto.getMessage());
            chat.setCreatedAt(new Date());

            Users sender = userRepository.findById(dto.getSendId())
                    .orElseThrow(() -> new IllegalArgumentException("보낸 사람 없음"));
            Users receiver = userRepository.findById(dto.getReceiveId())
                    .orElseThrow(() -> new IllegalArgumentException("받는 사람 없음"));

            chat.setSendID(sender);
            chat.setReceiveID(receiver);

            chatRepository.save(chat);
        }

        if (chatRoomSession.size() >= 3) {
            removeClosedSession(chatRoomSession);
        }

        sendMessageToChatRoom(dto, chatRoomSession);
    }

    private void removeClosedSession(Set<WebSocketSession> chatRoomSession) {
        chatRoomSession.removeIf(sess -> !sessions.contains(sess));
    }

    private void sendMessageToChatRoom(ChatMessageDto dto, Set<WebSocketSession> chatRoomSession) {
        chatRoomSession.parallelStream()
                .filter(sess -> {
                    Long userId = sessionUserMap.get(sess);
                    return userId != null && userId.equals(dto.getReceiveId());
                })
                .forEach(sess -> sendMessage(sess, dto));
    }

    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error("메시지 전송 실패", e);
        }
    }

    public List<Chat> getChatByUserId(Long userId) {
        return chatRepository.findAllBySendID_IdOrReceiveID_Id(userId, userId);
    }

    public ChatMessageDto createOrReturnChatRoom(Users sender, Users receiver) {
        // 1. 두 사용자의 기존 채팅이 존재하는지 확인 (양방향 모두)
        Optional<Chat> existingChat = chatRepository.findTopBySendIDAndReceiveIDOrReceiveIDAndSendIDOrderByCreatedAtDesc(
                sender, receiver, sender, receiver
        );

        ChatMessageDto dto = new ChatMessageDto();
        dto.setMessageType(ChatMessageDto.MessageType.ENTER);
        dto.setSendId(sender.getId());
        dto.setReceiveId(receiver.getId());
        dto.setCreatedAt(new Date().toString());

        if (existingChat.isPresent()) {
            dto.setChatId(existingChat.get().getChatId()); // 기존 채팅방 ID
            dto.setMessage("기존 채팅방으로 입장합니다.");
        } else {
            // 2. 새 채팅방을 만든다고 가정 → 채팅 메시지 하나 저장해서 chatId 확보
            Chat newChat = new Chat();
            newChat.setSendID(sender);
            newChat.setReceiveID(receiver);
            newChat.setMessage("채팅방이 생성되었습니다.");
            newChat.setCreatedAt(new Date());

            Chat saved = chatRepository.save(newChat);
            dto.setChatId(saved.getChatId());
            dto.setMessage("새 채팅방이 생성되었습니다.");
        }

        return dto;
    }

}

