package com.vpr42.marketplacefeedapi.controller;

import com.vpr42.marketplacefeedapi.model.dto.City;
import com.vpr42.marketplacefeedapi.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feed/cities")
public class CitiesController {
    private final CityService cityService;

    @GetMapping
    public ResponseEntity<Page<City>> getCities(
        @RequestParam(value = "withOrders", required = false, defaultValue = "false") boolean withOrders,
        @RequestParam(value = "query", required = false) String query,
        @RequestParam(value = "orderedByOrdersCount", required = false, defaultValue = "false") boolean orderedByOrdersCount,
        @RequestParam(value = "page", required = false, defaultValue = "0") int page,
        @RequestParam(value = "pageSize", required = false, defaultValue = "15") int pageSize
    ) {
        return ResponseEntity
                .ok(cityService.getCities(query,
                        withOrders,
                        orderedByOrdersCount,
                        page,
                        pageSize));
    }
}
