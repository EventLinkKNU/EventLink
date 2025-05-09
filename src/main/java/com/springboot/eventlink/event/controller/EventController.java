package com.springboot.eventlink.event.controller;

import com.springboot.eventlink.event.dto.EventCreateDto;
import com.springboot.eventlink.event.dto.EventParticipationDto;
import com.springboot.eventlink.event.dto.EventResponseDto;
import com.springboot.eventlink.event.entity.ApplicationStatus;
import com.springboot.eventlink.event.entity.EventParticipation;
import com.springboot.eventlink.event.service.EventParticipationService;
import com.springboot.eventlink.event.service.EventService;
import com.springboot.eventlink.user.dto.CustomOAuth2User;
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
    @Autowired
    public EventController(EventService eventService, EventParticipationService eventParticipationService) {
        this.eventService = eventService;
        this.eventParticipationService = eventParticipationService;
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

    //    이벤트 삭제 요청
    @DeleteMapping("/delete-event")
    public ResponseEntity<String> deleteEvent(@RequestParam Long eventId, @AuthenticationPrincipal CustomOAuth2User user){
        String userName = user.getUsername();
        eventService.deleteEvent(eventId, userName);
        return ResponseEntity.ok("이벤트 삭제 완");
    }

    //    이벤트 신청 요청
    @PostMapping("/events/apply")
    public ResponseEntity<Long> applyToEvent(@RequestBody EventParticipationDto dto,
                                             @AuthenticationPrincipal CustomOAuth2User userDetails) {
        Long participationId = eventParticipationService.applyToEvent(userDetails.getUsername(), dto);
        return ResponseEntity.ok(participationId);
    }

    //    (이벤트 개설자)내 이벤트 참여 신청서 보기
    @GetMapping("/event/apply/get-my-apply")
    public ResponseEntity<List<EventParticipationDto>> getMyApply(@RequestParam Long eventId, @AuthenticationPrincipal CustomOAuth2User user){
        String userName = user.getUsername();
        List<EventParticipationDto> eventParticipationDtos = eventParticipationService.getParticipationsByEvent(userName, eventId);
        System.out.println(eventParticipationDtos+ "-------------");
        return ResponseEntity.ok(eventParticipationDtos);
    }

    //    (이벤트 개설자)이벤트 신청서 상태 수정
//    @RequestParam String username -> 이거 그 사용자 테이블에서 보이는 MemberID (ex. google 116978770854110719034) 입력하는거임~~~
    @PatchMapping("/event/apply/update-status")
    public ResponseEntity<String> updateApplicationStatus(@RequestParam Long eventId, @RequestParam String username, @RequestParam ApplicationStatus status,
                                                          @AuthenticationPrincipal CustomOAuth2User user) {
        eventParticipationService.updateApplicationStatus(eventId, username, status);
        return ResponseEntity.ok("신청서 상태가 업데이트되었습니다.");
    }
}
