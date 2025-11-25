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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping
    public ResponseEntity<Page<Job>> getJobs(
            @ModelAttribute @Valid JobFilters filters,
            @AuthenticationPrincipal UserEntity user
    ) {
        return ResponseEntity
                .ok(jobService.getJobsFiltered(filters));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteJob(@PathVariable UUID id,
                          @AuthenticationPrincipal UserEntity currentUser) {
        log.info("Processing delete job request for job id: {} from user: {}", id, currentUser.getId());
        jobService.deleteJob(id, currentUser);
    }
}