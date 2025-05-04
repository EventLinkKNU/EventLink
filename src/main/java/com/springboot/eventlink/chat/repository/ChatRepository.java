package com.springboot.eventlink.chat.repository;

import com.springboot.eventlink.chat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}