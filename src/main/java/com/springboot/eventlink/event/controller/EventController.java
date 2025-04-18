package com.springboot.eventlink.event.controller;

import com.springboot.eventlink.event.dto.EventCreateDto;
import com.springboot.eventlink.event.dto.EventResponseDto;
import com.springboot.eventlink.event.service.EventService;
import com.springboot.eventlink.user.dto.CustomOAuth2User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/events")
public class EventController {
    private final EventService eventService;
    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createEvent(@AuthenticationPrincipal CustomOAuth2User user, @RequestBody EventCreateDto request){
        Long eventId = eventService.createEvent(user.getUsername(), request);
        if (eventId != null) {
//            URI location = URI.create("/api/events/" + eventId);
//            return ResponseEntity.created(location).body("Event created successfully");
            return ResponseEntity.ok(eventId);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create event");
        }

    }
    @GetMapping("/getMyEvents")
    public ResponseEntity<List<EventResponseDto>> getMyEventList(@AuthenticationPrincipal CustomOAuth2User user){
        String userName = user.getUsername();
        List<EventResponseDto> events = eventService.getUserEvents(userName);
        return ResponseEntity.ok(events);
    }
    @GetMapping("/getAllEvents")
    public ResponseEntity<List<EventResponseDto>> getALlEventList(@AuthenticationPrincipal CustomOAuth2User user){
        List<EventResponseDto> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }
    @DeleteMapping("/deleteEvent")
    public ResponseEntity<String> deleteEvent(@PathVariable Long eventId, @AuthenticationPrincipal CustomOAuth2User user){
        String userName = user.getUsername();
        eventService.deleteEvent(eventId, userName);
        return ResponseEntity.ok("이벤트 삭제 완");
    }
}
