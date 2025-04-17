package com.springboot.eventlink.event.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventCreateDto {
    private String title;
    private String content;
    private Integer minParticipants;
    private Integer maxParticipants;
    private LocalDateTime startDate;
    private LocalDateTime closeDate;
    private Long categoryId;
}