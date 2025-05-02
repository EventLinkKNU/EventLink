package com.springboot.eventlink.event.repository;

import com.springboot.eventlink.event.entity.EventApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventApplicationRepository extends JpaRepository<EventApplication, Long> {

}
