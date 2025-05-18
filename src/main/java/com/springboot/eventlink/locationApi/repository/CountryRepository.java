package com.springboot.eventlink.locationApi.repository;


import com.springboot.eventlink.locationApi.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Long> {
    Optional<Country> findByName(String name);
}