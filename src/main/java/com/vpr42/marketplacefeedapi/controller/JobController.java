package com.vpr42.marketplacefeedapi.controller;

import com.vpr42.marketplacefeedapi.model.dto.CreateJobDto;
import com.vpr42.marketplacefeedapi.model.dto.Job;
import com.vpr42.marketplacefeedapi.model.entity.UserEntity;
import com.vpr42.marketplacefeedapi.service.JobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/jobs")
@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
public class JobController {
    private final JobService jobService;

    @PostMapping
    public ResponseEntity<Job> createJob(@RequestBody CreateJobDto dto,
                                         @AuthenticationPrincipal UserEntity user) {
        log.info("Processing new create job request from user: {}", user.getId());
        Job result = jobService.createJob(dto, user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(result);
    }
}
