package com.springboot.eventlink.search.controller;

import com.springboot.eventlink.event.entity.Event;
import com.springboot.eventlink.search.dto.EventResponse;
import com.springboot.eventlink.search.dto.SearchDto;
import com.springboot.eventlink.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public List<EventResponse> search(@ModelAttribute SearchDto searchDto) {
        return searchService.searchEvents(searchDto);
    }
}
