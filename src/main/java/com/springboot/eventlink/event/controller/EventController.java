package com.springboot.eventlink.event.controller;

import com.springboot.eventlink.event.dto.EventCreateDto;
import com.springboot.eventlink.event.dto.EventFilterDto;
import com.springboot.eventlink.event.dto.EventParticipationDto;
import com.springboot.eventlink.event.dto.EventResponseDto;
import com.springboot.eventlink.event.entity.ApplicationStatus;
import com.springboot.eventlink.event.entity.EventParticipation;
import com.springboot.eventlink.event.service.EventParticipationService;
import com.springboot.eventlink.event.service.EventService;
import com.springboot.eventlink.user.dto.CustomOAuth2User;
import com.springboot.eventlink.user.dto.UserDTO;
import com.springboot.eventlink.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/events")
public class EventController {
    private final EventService eventService;
    private final EventParticipationService eventParticipationService;
    private final UserService userService;

    @Autowired
    public EventController(EventService eventService, EventParticipationService eventParticipationService, UserService userService) {
        this.eventService = eventService;
        this.eventParticipationService = eventParticipationService;
        this.userService = userService;
    }

    //    이벤트 개설 요청
    @PostMapping("/create")
    public ResponseEntity<?> createEvent(@AuthenticationPrincipal CustomOAuth2User user, @RequestBody EventCreateDto request){
        Long eventId = eventService.createEvent(user.getUsername(), request);
        if (eventId != null) {
            return ResponseEntity.ok(eventId);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create event");
        }
    }


    //   내가 개설한 이벤트 조회
    @GetMapping("/get-my-events")
    public ResponseEntity<List<EventResponseDto>> getMyEventList(@AuthenticationPrincipal CustomOAuth2User user){
        String userName = user.getUsername();
        List<EventResponseDto> events = eventService.getUserEvents(userName);
        return ResponseEntity.ok(events);
    }

    //    전체 이벤트 조회
    @GetMapping("/get-all-events")
    public ResponseEntity<List<EventResponseDto>> getALlEventList(@AuthenticationPrincipal CustomOAuth2User user){
        List<EventResponseDto> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    //  이벤트Id로 이벤트 불러오기
    @GetMapping("/get-event")
    public ResponseEntity<?> getEvent(@RequestParam Long eventId, @AuthenticationPrincipal CustomOAuth2User user){
        EventResponseDto eventResponseDto = eventService.getEventById(eventId);
        return ResponseEntity.ok(eventResponseDto);
    }

    //    이벤트 삭제 요청
    @DeleteMapping("/delete-event")
    public ResponseEntity<String> deleteEvent(@RequestParam Long eventId, @AuthenticationPrincipal CustomOAuth2User user){
        String userName = user.getUsername();
        eventService.deleteEvent(eventId, userName);
        return ResponseEntity.ok("이벤트 삭제 완");
    }

    //    이벤트 신청 요청
    @PostMapping("/apply")
    public ResponseEntity<Long> applyToEvent(@RequestBody EventParticipationDto dto,
                                             @AuthenticationPrincipal CustomOAuth2User userDetails) {
        Long participationId = eventParticipationService.applyToEvent(userDetails.getUsername(), dto);
        return ResponseEntity.ok(participationId);
    }

    //    (이벤트 개설자)내가 개설한 이벤트에 대한 참여 신청서 리스트 보기
    @GetMapping("/applications/event")
    public ResponseEntity<List<EventParticipationDto>> getMyApply(@RequestParam Long eventId, @AuthenticationPrincipal CustomOAuth2User user){
        String userName = user.getUsername();
        List<EventParticipationDto> eventParticipationDtos = eventParticipationService.getParticipationsForMyEvent(userName, eventId);
        return ResponseEntity.ok(eventParticipationDtos);
    }

    //    (이벤트 개설자)이벤트 신청서 상태 수정
//    @PatchMapping("/event/apply/update-status")
    @PatchMapping("/applications/event/update-status")
    public ResponseEntity<String> updateApplicationStatus(@RequestParam Long eventId, @RequestParam ApplicationStatus status,
                                                          @RequestParam String username) {
        eventParticipationService.updateApplicationStatus(eventId, username, status);
        return ResponseEntity.ok("신청서 상태가 업데이트되었습니다.");
    }
    //현재 로그인한 user정보 가져오기.
    @GetMapping("/get-current-user")
    public ResponseEntity<UserDTO> getCurrentUser(@AuthenticationPrincipal CustomOAuth2User user){
        UserDTO userInfo = userService.getUserInfo(user.getUsername());
        return ResponseEntity.ok(userInfo);
    }
    //  내가 참여한 신청서 보기
    @GetMapping("/applications/me")
    public ResponseEntity<List<EventParticipationDto>> getApply(@AuthenticationPrincipal CustomOAuth2User user){
        String userName = user.getUsername();
        List<EventParticipationDto> eventParticipationDtos = eventParticipationService.getParticipations(userName);
        return ResponseEntity.ok(eventParticipationDtos);
    }
    @GetMapping("/filter")
    public ResponseEntity<List<EventResponseDto>> getFilteredEvents(
            @ModelAttribute EventFilterDto filterDto
    ) {
        List<EventResponseDto> filteredEvents = eventService.getFilteredEvents(filterDto);
        return ResponseEntity.ok(filteredEvents);
    }
}
