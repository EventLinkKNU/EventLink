package com.springboot.eventlink.event.dto;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EventResponseDto {
    private Long id;
    private String title;
    private String content;
    private Integer minParticipants;
    private Integer maxParticipants;
    private LocalDateTime startDate;
    private LocalDateTime closeDate;
    private LocalDateTime createdAt;
    private Long creatorId;
    private String creatorName;

    private Long categoryId;
    private String categoryName;
}