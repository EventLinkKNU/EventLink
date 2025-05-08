package com.springboot.eventlink.scrap.service;

import com.springboot.eventlink.event.entity.Event;
import com.springboot.eventlink.event.repository.EventRepository;
import com.springboot.eventlink.scrap.dto.ScrapRequestDto;
import com.springboot.eventlink.scrap.dto.ScrapResponseDto;
import com.springboot.eventlink.scrap.entity.Scrap;
import com.springboot.eventlink.scrap.repository.ScrapRepository;
import com.springboot.eventlink.user.entity.Users;
import com.springboot.eventlink.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScrapService {

    private final ScrapRepository scrapRepository;
    private final UserRepository usersRepository;
    private final EventRepository eventRepository;

    @Transactional
    public Scrap createScrap(ScrapRequestDto dto) {
        Users user = usersRepository.findById(dto.getCreatorId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이벤트입니다."));

        Scrap scrap = new Scrap();
        scrap.setCreator(user);
        scrap.setEvent(event);

        return scrapRepository.save(scrap);
    }


    public List<ScrapResponseDto> getUserScraps(String email) {
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        List<Scrap> scraps = scrapRepository.findByCreator(user);

        return scraps.stream()
                .map(scrap -> {
                    Event event = scrap.getEvent();
                    return ScrapResponseDto.builder()
                            .scrapId(scrap.getId())
                            .eventId(event.getId())
                            .eventTitle(event.getTitle())
                            .eventContent(event.getContent())
                            .eventStartDate(event.getStartDate())
                            .eventCloseDate(event.getCloseDate())
                            .eventCreatorName(event.getCreator().getName())
                            .categoryId(event.getCategory().getId())
                            .categoryName(event.getCategory().getName())
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteScrap(Long scrapId, String email) {
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Scrap scrap = scrapRepository.findById(scrapId)
                .orElseThrow(() -> new IllegalArgumentException("스크랩을 찾을 수 없습니다."));

        if (!scrap.getCreator().getId().equals(user.getId())) {
            throw new IllegalStateException("삭제 권한이 없습니다.");
        }
        scrapRepository.delete(scrap);
    }



}
