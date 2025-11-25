package com.vpr42.marketplacefeedapi.controller;

import com.vpr42.marketplacefeedapi.model.dto.CategoryWithCount;
import com.vpr42.marketplacefeedapi.model.enums.SortType;
import com.vpr42.marketplacefeedapi.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Categories", description = "Контроллер категорий")
public class CategoriesController {
    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "Получение списка категорий", responses = {
        @ApiResponse(responseCode = "200",
            content = @Content(
                array = @ArraySchema(schema = @Schema(implementation = CategoryWithCount.class))
            )
        )
    })
    public ResponseEntity<List<CategoryWithCount>> getCategories(
        @Parameter(description = "Поисковый запрос")
        @RequestParam(value = "query", required = false)
        String query,
        @Parameter(description = "Сортировка по количеству услуг. Оставьте null, если не нужна", example = "DESC")
        @RequestParam(value = "jobsCountSort", required = false)
        SortType jobsCountSort
    ) {
        log.info("Processing get categories list request");
        var categories = categoryService.getOrdered(query, jobsCountSort);

        return ResponseEntity.ok(categories);
    }
}
