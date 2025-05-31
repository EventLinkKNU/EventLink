package com.springboot.eventlink.event.service;

import com.springboot.eventlink.event.dto.EventCreateDto;
import com.springboot.eventlink.event.dto.EventFilterDto;
import com.springboot.eventlink.event.dto.EventParticipationDto;
import com.springboot.eventlink.event.dto.EventResponseDto;
import com.springboot.eventlink.event.entity.*;
import com.springboot.eventlink.event.repository.CategoryRepository;
import com.springboot.eventlink.event.repository.EventApplicationRepository;
import com.springboot.eventlink.event.repository.EventParticipationRepository;
import com.springboot.eventlink.event.repository.EventRepository;
import com.springboot.eventlink.event.specification.EventSpecification;
import com.springboot.eventlink.user.dto.UserDTO;
import com.springboot.eventlink.user.entity.Users;
import com.springboot.eventlink.user.repository.UserRepository;
import com.springboot.eventlink.user.service.UserService;
import org.apache.catalina.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final EventParticipationRepository eventParticipationRepository;
    private final EventApplicationRepository eventApplicationRepository;
    private final UserService userService;

    public EventService(UserRepository userRepository, EventRepository eventRepository, CategoryRepository categoryRepository, EventParticipationRepository eventParticipationRepository, EventApplicationRepository eventApplicationRepository, UserService userService) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
        this.eventParticipationRepository = eventParticipationRepository;
        this.eventApplicationRepository = eventApplicationRepository;
        this.userService = userService;
    }
    //    이벤트 생성
    @Transactional
    public Long createEvent(String userName, EventCreateDto dto){
        Users creator = userRepository.findByUsername(userName);
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(()-> new IllegalArgumentException("카테고리 찾을 수 없음"));
        Event event = new Event();
        event.setCreator(creator);
        event.setCategory(category);
        event.setTitle(dto.getTitle());
        event.setContent(dto.getContent());
        event.setCountry(dto.getCountry());
        event.setCity(dto.getCity());
        event.setGenderFilter(dto.getGenderFilter());
        event.setStyleFilter(dto.getStyleFilter());
        event.setMinParticipants(dto.getMinParticipants());
        event.setCurrentParticipants(1);
        event.setMaxParticipants(dto.getMaxParticipants());
        event.setStartDate(dto.getStartDate());
        event.setCloseDate(dto.getCloseDate());

        return eventRepository.save(event).getId();
    }


    public EventResponseDto getEventById(Long eventId){
        Event event = eventRepository.findById(eventId)
                .orElseThrow(()-> new IllegalArgumentException("이벤트를 찾을 수 없습니다."));
        return toDto(event);
    }

    //    내 이벤트 조회
    public List<EventResponseDto> getUserEvents(String userName) {
        Users creator = userRepository.findByUsername(userName);
        return eventRepository.findByCreatorId(creator.getId()).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    //    해당 카테고리의 이벤트 조회
    public List<EventResponseDto> getEventsByCategory(Long categoryId) {
        return eventRepository.findByCategoryId(categoryId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    //    모든 이벤트 조회
    public List<EventResponseDto> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    public List<EventResponseDto> getFilteredEvents(EventFilterDto eventFilterDto) {
        Specification<Event> spec = EventSpecification.filter(eventFilterDto);
        List<Event> filteredEvents = eventRepository.findAll(spec);

        return filteredEvents.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }


    //    이벤트 삭제
    @Transactional
    public void deleteEvent(Long eventId, String userName) {
        Users user = userRepository.findByUsername(userName);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("이벤트를 찾을 수 없습니다."));
        if (!event.getCreator().getId().equals(user.getId())) {
            throw new IllegalStateException("삭제 권한이 없습니다.");
        }
        eventRepository.delete(event);
    }

    //    이벤트 전송용 데이터
    private EventResponseDto toDto(Event event) {
        System.out.println(event.getCreator().getUsername());
        return EventResponseDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .content(event.getContent())
                .country(event.getCountry())
                .city(event.getCity())
                .genderFilter(event.getGenderFilter())
                .styleFilter(event.getStyleFilter())
                .minParticipants(event.getMinParticipants())
                .maxParticipants(event.getMaxParticipants())
                .currentParticipants(event.getCurrentParticipants())
                .startDate(event.getStartDate())
                .closeDate(event.getCloseDate())
                .createdAt(event.getCreatedAt())
                .creatorId(event.getCreator().getId())
                .creatorUsername(event.getCreator().getUsername())
                .creatorName(event.getCreator().getName())
                .creatorCountry(event.getCreator().getCountry())
                .creatorGender(event.getCreator().getGender())
                .categoryId(event.getCategory().getId())
                .categoryName(event.getCategory().getName())
                .build();
    }
}
