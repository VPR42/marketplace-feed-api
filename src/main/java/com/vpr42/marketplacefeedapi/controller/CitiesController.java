package com.vpr42.marketplacefeedapi.controller;

import com.vpr42.marketplacefeedapi.model.dto.City;
import com.vpr42.marketplacefeedapi.service.CityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Cities", description = "Контроллер для городов")
public class CitiesController {
    private final CityService cityService;

    @GetMapping
    @Operation(summary = "Получение списка городов с фильтрами", responses = {
        @ApiResponse(responseCode = "200")
    })
    public ResponseEntity<Page<City>> getCities(
        @Parameter(description = "Запрашивать с услугами")
        @RequestParam(value = "withJobs", required = false, defaultValue = "false")
        boolean withJobs,

        @Parameter(description = "Поисковый запрос")
        @RequestParam(value = "query", required = false)
        String query,

        @Parameter(description = "Отсортировать по количеству заказов (NULL - без сортировки")
        @RequestParam(value = "orderedByJobsCount", required = false, defaultValue = "false")
        boolean orderedByJobsCount,

        @Parameter(description = "Номер страницы")
        @RequestParam(value = "page", required = false, defaultValue = "0")
        int page,

        @Parameter(description = "Размер страницы")
        @RequestParam(value = "pageSize", required = false, defaultValue = "15")
        int pageSize
    ) {
        return ResponseEntity
                .ok(cityService.getCities(query,
                        withJobs,
                        orderedByJobsCount,
                        page,
                        pageSize));
    }
}
