package com.springboot.eventlink.chat.entity;

import com.springboot.eventlink.user.entity.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "CHAT")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CHAT_ID")
    private Long chatId;

    @ManyToOne
    @JoinColumn(name = "SEND_ID_FK", nullable = false)
    private Users sendID;

    @ManyToOne
    @JoinColumn(name = "RECEIVE_ID_FK", nullable = false)
    private Users receiveID;

    @Column(name = "MESSAGE")
    private String message;

    @Column(name = "CREATED_AT")
    private Date createdAt;
}
