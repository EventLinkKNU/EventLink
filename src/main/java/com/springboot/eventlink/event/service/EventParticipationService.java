package com.springboot.eventlink.event.service;

import com.springboot.eventlink.event.entity.EventParticipation;
import com.springboot.eventlink.event.repository.EventParticipationRepository;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventParticipationService {
    private final EventParticipationRepository eventParticipationRepository;
    @Autowired
    public EventParticipationService(EventParticipationRepository eventParticipationRepository) {
        this.eventParticipationRepository = eventParticipationRepository;
    }
    public EventParticipation save(EventParticipation eventParticipation) {
        return eventParticipationRepository.save(eventParticipation);
    }
}
