package com.springboot.eventlink.chat.controller;

import com.springboot.eventlink.chat.dto.ChatMessageDto;
import com.springboot.eventlink.chat.entity.Chat;
import com.springboot.eventlink.chat.repository.ChatRepository;
import com.springboot.eventlink.chat.service.ChatService;
import com.springboot.eventlink.user.entity.Users;
import com.springboot.eventlink.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;

    //사용자가 참여 중인 채팅방 리스트 보기
    @PostMapping("/{userId}")
    public ResponseEntity<List<Chat>> getUserUserChatRooms(@PathVariable("userId") Long userId) {
        List<Chat> chatList = chatService.getChatByUserId(userId);
        return ResponseEntity.ok(chatList);
    }

    //이벤트 상세 화면에서 채팅 시작 (방 생성 or 기존 방 반환)
    @PostMapping("/create/{userId}")
    public ResponseEntity<ChatMessageDto> createChatRoom(@PathVariable("userId") Long userId, @RequestBody ChatMessageDto request) {
        Users sender = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("보내는 사용 없음"));

        Users receiver = userRepository.findById(request.getReceiveId()).orElseThrow(() -> new IllegalArgumentException("받는 사용자 없음"));

        ChatMessageDto result = chatService.createOrReturnChatRoom(sender, receiver);
        result.setReceiveId(receiver.getId());
        return ResponseEntity.ok(result);
    }
    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<Chat>> getChatMessages(@PathVariable("roomId") Long roomId) {
        List<Chat> messages = chatRepository.findAllByRoomIdOrderByCreatedAtAsc(roomId);
        return ResponseEntity.ok(messages);
    }


}
