package com.springboot.eventlink.event.dto;

import lombok.Data;

@Data
public class EventParticipationDto {
    private Long eventId;
    private String content;
}
