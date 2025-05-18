package com.springboot.eventlink.event.service;

import com.springboot.eventlink.event.dto.EventParticipationDto;
import com.springboot.eventlink.event.entity.ApplicationStatus;
import com.springboot.eventlink.event.entity.Event;
import com.springboot.eventlink.event.entity.EventApplication;
import com.springboot.eventlink.event.entity.EventParticipation;
import com.springboot.eventlink.event.repository.EventApplicationRepository;
import com.springboot.eventlink.event.repository.EventParticipationRepository;
import com.springboot.eventlink.event.repository.EventRepository;
import com.springboot.eventlink.user.entity.Users;
import com.springboot.eventlink.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventParticipationService {
    private final EventParticipationRepository eventParticipationRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventApplicationRepository eventApplicationRepository;

    @Autowired
    public EventParticipationService(EventParticipationRepository eventParticipationRepository, EventRepository eventRepository, UserRepository userRepository, EventApplicationRepository eventApplicationRepository) {
        this.eventParticipationRepository = eventParticipationRepository;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.eventApplicationRepository = eventApplicationRepository;
    }

    //    이벤트 신청
    @Transactional
    public Long applyToEvent(String userName, EventParticipationDto dto) {
        Users user = userRepository.findByUsername(userName);
        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new IllegalArgumentException("이벤트를 찾을 수 없습니다."));

        //이미 신청한 사람 거르기
        if (eventApplicationRepository.existsByEventAndMember(event, user)) {
            throw new IllegalStateException("이미 이 이벤트에 신청한 사용자입니다.");
        }

        // 참여 정보 저장
        EventParticipation participation = new EventParticipation();
        participation.setEvent(event);
        participation.setMember(user);
        participation.setContent(dto.getContent());
        eventParticipationRepository.save(participation);

        // 신청 처리 정보 저장
        EventApplication application = new EventApplication();
        application.setEvent(event);
        application.setMember(user);
        application.setStatus(ApplicationStatus.PENDING);
        application.setApplicationDate(LocalDateTime.now());
        eventApplicationRepository.save(application);

        return participation.getId();
    }


    //    내 이벤트별 신청서 조회
    public List<EventParticipationDto> getParticipationsByEvent(String username, Long eventId) {
        Users user = userRepository.findByUsername(username);
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("해당 이벤트가 없습니다."));
        if (!user.getId().equals(event.getCreator().getId())) {
            throw new IllegalStateException("이벤트 작성자가 아닙니다.");
        }

        return eventParticipationRepository.findAllByEvent(event).stream()
                .map(this::toParticipationDto)
                .collect(Collectors.toList());
    }

    private EventParticipationDto toParticipationDto(EventParticipation eventParticipation) {
        EventApplication application = eventApplicationRepository.findByEventAndMember(eventParticipation.getEvent(),eventParticipation.getMember());

        return EventParticipationDto.builder()
                .eventId(eventParticipation.getEvent().getId())
                .username(eventParticipation.getMember().getName())
                .content(eventParticipation.getContent())
                .applicationStatus(application != null ? application.getStatus() : null)
                .build();
    }

    //    이벤트 신청서 상태 변경
    @Transactional
    public void updateApplicationStatus(Long eventId, String username, ApplicationStatus status) {
        Users user = userRepository.findByUsername(username);
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new IllegalArgumentException("이벤트가 없습니다."));
        EventApplication application = eventApplicationRepository.findByEventAndMember(event, user);

        if (application == null) {
            throw new IllegalArgumentException("신청서를 찾을 수 없습니다.");
        }
        if(status == ApplicationStatus.PENDING) {
            application.setStatus(status);
            event.setCurrentParticipants(event.getCurrentParticipants() + 1);
        }else
            application.setStatus(status);
        eventApplicationRepository.save(application);
    }

}
