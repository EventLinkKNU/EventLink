package com.springboot.eventlink.event.dto;

import com.springboot.eventlink.event.entity.GenderFilter;
import com.springboot.eventlink.event.entity.StyleFilter;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventCreateDto {
    private String title;
    private String content;
    private String country;
    private String city;
    private GenderFilter genderFilter;
    private StyleFilter styleFilter;
    private Integer minParticipants;
    private Integer maxParticipants;
    private LocalDateTime startDate;
    private LocalDateTime closeDate;
    private Long categoryId;
}