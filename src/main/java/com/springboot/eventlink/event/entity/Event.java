package com.springboot.eventlink.event.entity;

import com.springboot.eventlink.scrap.entity.Scrap;
import com.springboot.eventlink.user.entity.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

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

    private String country;

    private String city;

    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "GENDER_FILTER")
    private GenderFilter genderFilter;

    @Enumerated(EnumType.STRING)
    @Column(name = "STYLE_FILTER")
    private StyleFilter styleFilter;

    @Column(name = "MIN_PARTICIPANTS")
    private Integer minParticipants;

    @Column(name = "MAX_PARTICIPANTS")
    private Integer maxParticipants;

    @Column(name = "CURRENT_PARTICIPANTS")
    private Integer currentParticipants;

    @Column(name = "EVENT_START_DATE")
    private LocalDateTime startDate;

    @Column(name = "EVENT_CLOSE_DATE")
    private LocalDateTime closeDate;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "event", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<EventApplication> applications;

    @OneToMany(mappedBy = "event", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<EventParticipation> participations;
    @OneToMany(mappedBy = "event", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Scrap> scraps;
}

