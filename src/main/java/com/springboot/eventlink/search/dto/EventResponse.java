package com.springboot.eventlink.search.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class EventResponse {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
}
