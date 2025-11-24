package com.vpr42.marketplacefeedapi.controller;

import com.vpr42.marketplacefeedapi.model.dto.CreateJobDto;
import com.vpr42.marketplacefeedapi.model.dto.Job;
import com.vpr42.marketplacefeedapi.model.dto.JobFilters;
import com.vpr42.marketplacefeedapi.model.entity.UserEntity;
import com.vpr42.marketplacefeedapi.service.JobService;
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
public class JobController {
    private final JobService jobService;

    @PostMapping
    public ResponseEntity<Job> createJob(@RequestBody @Valid CreateJobDto dto,
                                       @AuthenticationPrincipal UserEntity user) {
        log.info("Processing new create job request from user: {}", user.getId());
        Job result = jobService.createJob(dto, user);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJobById(@PathVariable UUID id) {
        log.info("Processing get job request. jobId={}", id);
        Job result = jobService.getJobById(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Job> updateJob(@PathVariable UUID id,
                                         @RequestBody @Valid CreateJobDto dto,
                                         @AuthenticationPrincipal UserEntity user) {
        log.info("Processing update job request. jobId={}, userId={}", id, user.getId());
        Job result = jobService.updateJob(id, dto, user);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<Page<Job>> getJobs(
        @ModelAttribute @Valid JobFilters filters,
        @AuthenticationPrincipal UserEntity user
    ) {
        return ResponseEntity
                .ok(jobService.getJobsFiltered(filters));
    }
}
