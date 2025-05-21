package com.springboot.eventlink.search.service;

import com.springboot.eventlink.event.entity.Event;
import com.springboot.eventlink.search.dto.EventResponse;
import com.springboot.eventlink.search.dto.SearchDto;
import com.springboot.eventlink.search.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final SearchRepository searchRepository;

    public List<EventResponse> searchEvents(SearchDto dto) {
        String keyword = dto.getKeyword();
        List<Event> events = searchRepository.findByTitleContainingIgnoreCase(keyword);

        return events.stream()
                .map(e -> new EventResponse(
                        e.getId(),
                        e.getTitle(),
                        e.getContent(),
                        e.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}
