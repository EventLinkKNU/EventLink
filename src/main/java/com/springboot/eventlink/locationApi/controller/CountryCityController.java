package com.springboot.eventlink.locationApi.controller;


import com.springboot.eventlink.locationApi.entity.City;
import com.springboot.eventlink.locationApi.entity.Country;
import com.springboot.eventlink.locationApi.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/locations")
public class CountryCityController {

    private final CountryRepository countryRepository;

    // ✅ [1] 전체 국가 리스트 조회
    @GetMapping("/countries")
    public ResponseEntity<List<String>> getAllCountries() {
        List<String> countryNames = countryRepository.findAll().stream()
                .map(Country::getName)  // 또는 getKoreanName() 사용 가능
                .collect(Collectors.toList());
        return ResponseEntity.ok(countryNames);
    }

    // ✅ [2] 특정 국가의 도시 리스트 조회
    @GetMapping("/countries/{countryName}/cities")
    public ResponseEntity<List<String>> getCitiesByCountry(@PathVariable String countryName) {
        return countryRepository.findByName(countryName)
                .map(country -> country.getCities().stream()
                        .map(City::getName)
                        .collect(Collectors.toList()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

