package com.springboot.eventlink.event.repository;

import com.springboot.eventlink.event.entity.Event;
import com.springboot.eventlink.event.entity.EventApplication;
import com.springboot.eventlink.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventApplicationRepository extends JpaRepository<EventApplication, Long> {
        boolean existsByEventAndMember(Event event, Users member);
        void deleteAllByEvent(Event event);
        List<EventApplication> findByEvent(Event event);
        EventApplication findByEventAndMember(Event event, Users member);

}
