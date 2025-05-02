package com.springboot.eventlink.event.repository;

import com.springboot.eventlink.event.entity.Event;
import com.springboot.eventlink.event.entity.EventParticipation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventParticipationRepository extends JpaRepository<EventParticipation, Long> {
    List<EventParticipation> findByEventId(Long eventId);
}
