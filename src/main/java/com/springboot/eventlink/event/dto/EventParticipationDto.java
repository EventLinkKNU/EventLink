package com.springboot.eventlink.event.dto;

import com.springboot.eventlink.event.entity.ApplicationStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EventParticipationDto {
    private Long eventId;
    private String username;
    private String content;
    private ApplicationStatus applicationStatus;
}

