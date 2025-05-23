package com.springboot.eventlink.scrap.repository;

import com.springboot.eventlink.event.entity.Event;
import com.springboot.eventlink.scrap.entity.Scrap;
import com.springboot.eventlink.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    List<Scrap> findByCreator(Users creator);
    boolean existsByCreatorAndEvent(Users creator, Event event);
    Optional<Scrap> findByCreatorAndEvent(Users creator, Event event);
}
