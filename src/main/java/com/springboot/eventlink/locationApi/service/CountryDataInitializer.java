package com.springboot.eventlink.locationApi.service;

import com.springboot.eventlink.locationApi.entity.City;
import com.springboot.eventlink.locationApi.entity.Country;
import com.springboot.eventlink.locationApi.repository.CityRepository;
import com.springboot.eventlink.locationApi.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryDataInitializer {

    private final RestTemplate restTemplate = new RestTemplate();
    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;

    public void fetchAndStoreCountriesAndCities() {
        String url = "https://countriesnow.space/api/v0.1/countries";
        ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> body = response.getBody();
            List<Map<String, Object>> data = (List<Map<String, Object>>) body.get("data");

            for (Map<String, Object> countryData : data) {
                String countryName = (String) countryData.get("country");
                List<String> cities = (List<String>) countryData.get("cities");

                if (countryRepository.findByName(countryName).isPresent()) continue;

                Country country = new Country();
                country.setName(countryName);
                countryRepository.save(country);

                List<City> cityEntities = cities.stream()
                        .map(cityName -> {
                            City city = new City();
                            city.setName(cityName);
                            city.setCountry(country);
                            return city;
                        }).collect(Collectors.toList());

                cityRepository.saveAll(cityEntities);
            }
        }
    }
}
