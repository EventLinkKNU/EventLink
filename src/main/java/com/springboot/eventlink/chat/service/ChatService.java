package com.springboot.eventlink.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.eventlink.chat.dto.ChatMessageDto;
import com.springboot.eventlink.chat.entity.Chat;
import com.springboot.eventlink.chat.repository.ChatRepository;
import com.springboot.eventlink.user.entity.Users;
import com.springboot.eventlink.user.repository.UserRepository;
import jakarta.transaction.Transactional;
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
        Long chatRoomId = dto.getRoomId(); // ✅ 이제 roomId 사용

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
            chat.setRoomId(chatRoomId); // ✅ roomId 저장

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

    //    public ChatMessageDto createOrReturnChatRoom(Users sender, Users receiver) {
//        // 1️⃣ 기존 채팅방 (roomId) 조회
//        Optional<Chat> existingChat = chatRepository.findTopBySendIDAndReceiveIDOrReceiveIDAndSendIDOrderByCreatedAtDesc(
//                sender, receiver, receiver, sender
//        );
//
//        Long roomId;
//        if (existingChat.isPresent()) {
//            roomId = existingChat.get().getRoomId(); // ✅ 기존 roomId 사용
//        } else {
//            // 2️⃣ 새 roomId 생성 (max + 1)
//            Long maxRoomId = chatRepository.findAll().stream()
//                    .map(Chat::getRoomId)
//                    .filter(Objects::nonNull)
//                    .max(Long::compare)
//                    .orElse(0L);
//
//            roomId = maxRoomId + 1;
//        }
//
//        // 3️⃣ DTO 생성
//        ChatMessageDto dto = new ChatMessageDto();
//        dto.setMessageType(ChatMessageDto.MessageType.ENTER);
//        dto.setSendId(sender.getId());
//        dto.setReceiveId(receiver.getId());
//        dto.setCreatedAt(new Date().toString());
//        dto.setRoomId(roomId); // ✅ roomId 추가
//
//        if (existingChat.isPresent()) {
//            dto.setChatId(existingChat.get().getChatId());
//            dto.setRoomId(roomId);
//            dto.setMessage("기존 채팅방으로 입장합니다.");
//        } else {
//            // 4️⃣ 새 Chat 저장 (더미 메시지)
//            Chat newChat = new Chat();
//            newChat.setSendID(sender);
//            newChat.setReceiveID(receiver);
//            newChat.setRoomId(roomId); // ✅ roomId 저장
//            newChat.setMessage("채팅방이 생성되었습니다.");
//            newChat.setCreatedAt(new Date());
//
//            Chat savedChat = chatRepository.save(newChat);
//            dto.setChatId(savedChat.getChatId());
//            dto.setRoomId(roomId);
//            dto.setMessage("새 채팅방이 생성되었습니다.");
//        }
//
//        return dto;
//    }
    public ChatMessageDto createOrReturnChatRoom(Users sender, Users receiver) {
        // 항상 sender < receiver 순서로 정렬 → 관계 일관성
        Long senderId = sender.getId();
        Long receiverId = receiver.getId();

        if (senderId > receiverId) {
            Long temp = senderId;
            senderId = receiverId;
            receiverId = temp;
        }

        // 기존 roomId 찾기
        Optional<Chat> existingChat = chatRepository
                .findTopBySendIDAndReceiveIDOrReceiveIDAndSendIDOrderByCreatedAtDesc(
                        sender,
                        receiver,
                        receiver,
                        sender
                );


        Long roomId;
        if (existingChat.isPresent() && existingChat.get().getRoomId() != null) {
            roomId = existingChat.get().getRoomId();
        } else {
            // 새 roomId 생성
            Long maxRoomId = chatRepository.findAll().stream()
                    .map(Chat::getRoomId)
                    .filter(Objects::nonNull)
                    .max(Long::compare)
                    .orElse(0L);

            roomId = maxRoomId + 1;
        }

        // DTO 생성
        ChatMessageDto dto = new ChatMessageDto();
        dto.setMessageType(ChatMessageDto.MessageType.ENTER);
        dto.setSendId(sender.getId());
        dto.setReceiveId(receiver.getId());
        dto.setCreatedAt(new Date().toString());
        dto.setRoomId(roomId);

        if (existingChat.isPresent()) {
            dto.setChatId(existingChat.get().getChatId());
            dto.setMessage("기존 채팅방으로 입장합니다.");

            // 기존 chat row 에 roomId 가 없으면 업데이트 (migration 처리)
            if (existingChat.get().getRoomId() == null) {
                Chat existing = existingChat.get();
                existing.setRoomId(roomId);
                chatRepository.save(existing);
            }
        } else {
            // 새 Chat 저장 (dummy message)
            Chat newChat = new Chat();
            newChat.setSendID(sender);
            newChat.setReceiveID(receiver);
            newChat.setRoomId(roomId);
            newChat.setMessage("채팅방이 생성되었습니다.");
            newChat.setCreatedAt(new Date());

            Chat savedChat = chatRepository.save(newChat);
            dto.setChatId(savedChat.getChatId());
            dto.setMessage("새 채팅방이 생성되었습니다.");
        }

        return dto;
    }

    public List<Chat> getChatByRoomId(Long roomId) {
        return chatRepository.findAllByRoomIdOrderByCreatedAtAsc(roomId);
    }
    // ChatService 에 임시로 추가해서 한번 실행

    @Transactional
    public void migrateRoomIds() {
        List<Chat> chats = chatRepository.findAll();

        Map<String, Long> userPairToRoomId = new HashMap<>();
        long nextRoomId = chatRepository.findAll().stream()
                .map(Chat::getRoomId)
                .filter(Objects::nonNull)
                .max(Long::compare)
                .orElse(0L) + 1;

        for (Chat chat : chats) {
            Long senderId = chat.getSendID().getId();
            Long receiverId = chat.getReceiveID().getId();

            // sender < receiver 정렬
            if (senderId > receiverId) {
                Long temp = senderId;
                senderId = receiverId;
                receiverId = temp;
            }

            String key = senderId + "-" + receiverId;

            if (chat.getRoomId() == null) {
                // roomId 없으면 새로 지정
                if (!userPairToRoomId.containsKey(key)) {
                    userPairToRoomId.put(key, nextRoomId++);
                }

                chat.setRoomId(userPairToRoomId.get(key));
                chatRepository.save(chat);
            } else {
                // 기존 roomId 있으면 그대로 기록
                userPairToRoomId.putIfAbsent(key, chat.getRoomId());
            }
        }

        log.info("RoomId migration 완료!");
    }

}
