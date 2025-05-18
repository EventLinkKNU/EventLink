package com.springboot.eventlink.locationApi.repository;

import com.springboot.eventlink.locationApi.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long> {
}
