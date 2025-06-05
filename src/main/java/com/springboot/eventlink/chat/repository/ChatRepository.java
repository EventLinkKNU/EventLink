package com.springboot.eventlink.chat.repository;

import com.springboot.eventlink.chat.entity.Chat;
import com.springboot.eventlink.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    Optional<Chat> findTopBySendIDAndReceiveIDOrReceiveIDAndSendIDOrderByCreatedAtDesc(
            Users send1, Users receive1,
            Users send2, Users receive2
    );

    Optional<Chat> findFirstByRoomIdIsNotNullAndSendIDAndReceiveIDOrRoomIdIsNotNullAndReceiveIDAndSendIDOrderByCreatedAtDesc(
            Users send, Users receive, Users receive2, Users send2
    );


    List<Chat> findAllBySendID_IdOrReceiveID_Id(Long sendId, Long receiveId);

    List<Chat> findAllByRoomIdOrderByCreatedAtAsc(Long roomId);
}