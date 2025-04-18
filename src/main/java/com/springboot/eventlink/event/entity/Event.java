package com.springboot.eventlink.event.entity;

import com.springboot.eventlink.user.entity.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "EVENTS")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EVENT_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CREATOR_ID", nullable = false)
    private Users creator;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    private Category category;

    private String title;
    private String content;

    @Column(name = "MIN_PARTICIPANTS")
    private Integer minParticipants;

    @Column(name = "MAX_PARTICIPANTS")
    private Integer maxParticipants;

    @Column(name = "EVENT_START_DATE")
    private LocalDateTime startDate;

    @Column(name = "EVENT_CLOSE_DATE")
    private LocalDateTime closeDate;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt = LocalDateTime.now();
}
