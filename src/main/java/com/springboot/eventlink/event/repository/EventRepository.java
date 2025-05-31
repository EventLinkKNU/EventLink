package com.springboot.eventlink.event.repository;

import com.springboot.eventlink.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface EventRepository extends JpaRepository<Event,Long>, JpaSpecificationExecutor<Event> {
    List<Event> findByCreatorId(Long userId);
    List<Event> findByCategoryId(Long categoryId);

    // 제목에 키워드가 포함된 이벤트 조회
    List<Event> findByTitleContaining(String keyword);


}
