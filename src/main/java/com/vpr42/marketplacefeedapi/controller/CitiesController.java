package com.vpr42.marketplacefeedapi.controller;

import com.vpr42.marketplacefeedapi.model.dto.City;
import com.vpr42.marketplacefeedapi.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feed/cities")
public class CitiesController {
    private final CityService cityService;

    @GetMapping
    public ResponseEntity<Page<City>> getCities() {
        return ResponseEntity
                .ok(cityService.getCities());
    }
}
