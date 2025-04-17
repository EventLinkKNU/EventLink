package com.springboot.eventlink.event.entity;

import com.springboot.eventlink.user.entity.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "EVENT_PARTICIPATIONS", uniqueConstraints = @UniqueConstraint(columnNames = {"EVENT_ID", "MEMBER_ID"}))
public class EventParticipation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PARTICIPATION_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "EVENT_ID", nullable = false)
    private Event event;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Users member;

    private String content;

    @Column(name = "JOIN_DATE")
    private LocalDateTime joinDate = LocalDateTime.now();

    @Column(name = "LEAVE_DATE")
    private LocalDateTime leaveDate;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt = LocalDateTime.now();
}
