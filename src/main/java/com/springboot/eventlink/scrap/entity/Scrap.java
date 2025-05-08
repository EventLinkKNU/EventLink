package com.springboot.eventlink.scrap.entity;

import com.springboot.eventlink.event.entity.Event;
import com.springboot.eventlink.user.entity.Users;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "SCRAP")
public class Scrap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SCRAP_NUM_PK")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_NUM_FK", nullable = false)
    private Users creator;

    @ManyToOne
    @JoinColumn(name = "EVENT_NUM_FK", nullable = false)
    private Event event;
}
