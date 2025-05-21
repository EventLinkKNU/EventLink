package com.springboot.eventlink.search.repository;

import com.springboot.eventlink.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SearchRepository extends JpaRepository<Event, Long>  {

    List<Event> findByTitleContainingIgnoreCase(String keyword);

}
