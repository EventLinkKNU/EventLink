package com.springboot.eventlink.event.specification;

import com.springboot.eventlink.event.dto.EventFilterDto;
import com.springboot.eventlink.event.entity.Event;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class EventSpecification {
    public static Specification<Event> filter(EventFilterDto filter) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (filter.getCountry() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("country"), filter.getCountry()));
            }
            if (filter.getCity() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("city"), filter.getCity()));
            }
            if (filter.getCategoryId() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("category").get("id"), filter.getCategoryId()));
            }
            if (filter.getStartDate() != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("startDate"), filter.getStartDate().toLocalDate()));
            }
            if (filter.getCloseDate() != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("closeDate"), filter.getCloseDate().toLocalDate()));
            }
            if (filter.getMinParticipants() != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("minParticipants"), filter.getMinParticipants()));
            }
            if (filter.getMaxParticipants() != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("maxParticipants"), filter.getMaxParticipants()));
            }

            return predicate;
        };
    }
}
