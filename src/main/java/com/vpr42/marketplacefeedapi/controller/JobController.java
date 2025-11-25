package com.vpr42.marketplacefeedapi.controller;

import com.vpr42.marketplacefeedapi.model.dto.ApiErrorResponse;
import com.vpr42.marketplacefeedapi.model.dto.CreateJobDto;
import com.vpr42.marketplacefeedapi.model.dto.Job;
import com.vpr42.marketplacefeedapi.model.dto.JobFilters;
import com.vpr42.marketplacefeedapi.model.entity.UserEntity;
import com.vpr42.marketplacefeedapi.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/api/feed/jobs")
@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Jobs", description = "Контроллер для работы с услугами")
public class JobController {
    private final JobService jobService;

    @PostMapping
    @Operation(summary = "Создание услуги")
    @ApiResponse(responseCode = "200", description = "Услуга создана",
        content = @Content(schema = @Schema(implementation = Job.class)))
    @ApiResponse(responseCode = "400", description = "Ошибки валидации",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Переданные Тэги/Категория не существуют",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Услуга уже существует",
        content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    public ResponseEntity<Job> createJob(@RequestBody @Valid CreateJobDto dto,
                                       @AuthenticationPrincipal UserEntity user) {
        log.info("Processing new create job request from user: {}", user.getId());
        Job result = jobService.createJob(dto, user);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение по Id")
    @ApiResponse(responseCode = "200", description = "Услуга найдена",
            content = @Content(schema = @Schema(implementation = Job.class)))
    @ApiResponse(responseCode = "404", description = "Услуга не найдена",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    public ResponseEntity<Job> getJobById(@PathVariable UUID id) {
        log.info("Processing get job request. jobId={}", id);
        Job result = jobService.getJobById(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "Услуга создана",
            content = @Content(schema = @Schema(implementation = Job.class)))
    @ApiResponse(responseCode = "400", description = "Ошибки валидации",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "403", description = "Ошибка доступа к услуге",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "404", description = "Переданные Тэги/Категория/Услуга не существуют",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "409", description = "Услуга уже существует",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    public ResponseEntity<Job> updateJob(@PathVariable UUID id,
                                         @RequestBody @Valid CreateJobDto dto,
                                         @AuthenticationPrincipal UserEntity user) {
        log.info("Processing update job request. jobId={}, userId={}", id, user.getId());
        Job result = jobService.updateJob(id, dto, user);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    @ApiResponse(responseCode = "200", description = "Услуги найдены",
            content = @Content(schema = @Schema(implementation = Job.class)))
    @ApiResponse(responseCode = "404", description = "Услуги не найдены",
            content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    public ResponseEntity<Page<Job>> getJobs(
        @ModelAttribute @Valid JobFilters filters,
        @AuthenticationPrincipal UserEntity user
    ) {
        return ResponseEntity
                .ok(jobService.getJobsFiltered(filters));
    }
}
