package com.vpr42.marketplacefeedapi.service.impl;

import com.vpr42.marketplacefeedapi.model.dto.Job;
import com.vpr42.marketplacefeedapi.model.entity.UserEntity;
import com.vpr42.marketplacefeedapi.service.JobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobSecurityService {
    private final JobService jobService;

    public boolean hasAccess(UUID jobId, Authentication authentication) {
        UserEntity user = (UserEntity) authentication.getPrincipal();
        Job job = jobService.getJobById(jobId);
        if (job == null) {
            log.info("User is trying to access non existent resource");
            return true;
        }

        if (user == null || user.getId() != job.user().id()) {
            log.warn("User doesn't have access for modification");
            return false;
        }

        log.info("User has access for modification");
        return true;
    }
}
