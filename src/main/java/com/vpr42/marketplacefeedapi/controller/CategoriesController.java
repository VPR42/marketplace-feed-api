package com.vpr42.marketplacefeedapi.controller;

import com.vpr42.marketplacefeedapi.model.dto.CategoryWithCount;
import com.vpr42.marketplacefeedapi.model.enums.SortType;
import com.vpr42.marketplacefeedapi.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/feed/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoriesController {
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryWithCount>> getCategories(
        @RequestParam(value = "query", required = false) String query,
        @RequestParam(value = "jobsCountSort", required = false) SortType jobsCountSort
    ) {
        log.info("Processing get categories list request");
        var categories = categoryService.getOrdered(query, jobsCountSort);

        return ResponseEntity.ok(categories);
    }
}
