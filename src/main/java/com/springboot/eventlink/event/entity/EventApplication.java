package com.springboot.eventlink.event.entity;

import com.springboot.eventlink.user.entity.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "EVENT_APPLICATIONS", uniqueConstraints = @UniqueConstraint(columnNames = {"EVENT_ID", "MEMBER_ID"}))
public class EventApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "APPLICATION_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "EVENT_ID", nullable = false)
    private Event event;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Users member;

    @Enumerated(EnumType.STRING)
    @Column(name = "APPLICATION_STATUS")
    private ApplicationStatus status = ApplicationStatus.PENDING;

    @Column(name = "APPLICATION_DATE")
    private LocalDateTime applicationDate = LocalDateTime.now();

    @Column(name = "APPROVAL_DATE")
    private LocalDateTime approvalDate;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt = LocalDateTime.now();
}

