package com.springboot.eventlink.event.repository;

import com.springboot.eventlink.event.entity.Event;
import com.springboot.eventlink.event.entity.EventApplication;
import com.springboot.eventlink.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventApplicationRepository extends JpaRepository<EventApplication, Long> {
        boolean existsByEventAndMember(Event event, Users member);
}
