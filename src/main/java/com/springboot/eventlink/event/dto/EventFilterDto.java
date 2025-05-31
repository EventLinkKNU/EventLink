package com.springboot.eventlink.event.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

    @Data
    @Builder
    public class EventFilterDto {
        private String country;
        private String city;
        private Long categoryId;
        private LocalDateTime startDate;
        private LocalDateTime closeDate;
        private Integer minParticipants;
        private Integer maxParticipants;
    }
