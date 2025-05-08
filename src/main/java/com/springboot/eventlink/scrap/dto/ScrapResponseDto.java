package com.springboot.eventlink.scrap.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ScrapResponseDto {
    private Long scrapId;
    private Long eventId;
    private String eventTitle;
    private String eventContent;
    private LocalDateTime eventStartDate;
    private LocalDateTime eventCloseDate;
    private String eventCreatorName;
    private Long categoryId;
    private String categoryName;
}