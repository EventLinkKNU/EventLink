package com.springboot.eventlink.event.dto;


import com.springboot.eventlink.event.entity.GenderFilter;
import com.springboot.eventlink.event.entity.StyleFilter;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class EventResponseDto {
    private Long id;
    private String title;
    private String content;
    private String country;
    private String city;
    private GenderFilter genderFilter;
    private StyleFilter styleFilter;
    private Integer minParticipants;
    private Integer maxParticipants;
    private Integer currentParticipants;
    private LocalDateTime startDate;
    private LocalDateTime closeDate;
    private LocalDateTime createdAt;
    private Long creatorId;
    private String creatorUsername; // 이름 말고, Google 23812~~ 같은거.
    private String creatorName; // 이름
    private String creatorCountry;
    private String creatorGender;

    private Long categoryId;
    private String categoryName;
}