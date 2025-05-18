package com.springboot.eventlink.locationApi.controller;


import com.springboot.eventlink.locationApi.service.CountryDataInitializer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/init")
public class CountryInitController {

    private final CountryDataInitializer initializer;

    @PostMapping("/countries")
    public ResponseEntity<String> initCountries() {
        initializer.fetchAndStoreCountriesAndCities();
        return ResponseEntity.ok("국가/도시 데이터 저장 완료");
    }
}