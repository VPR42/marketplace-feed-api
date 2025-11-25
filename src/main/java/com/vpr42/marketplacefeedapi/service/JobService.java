package com.vpr42.marketplacefeedapi.service;

import com.vpr42.marketplacefeedapi.model.dto.CreateJobDto;
import com.vpr42.marketplacefeedapi.model.dto.Job;
import com.vpr42.marketplacefeedapi.model.dto.JobFilters;
import com.vpr42.marketplacefeedapi.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import java.util.UUID;

public interface JobService {
    Job createJob(CreateJobDto dto, UserEntity initiator);
    Page<Job> getJobsFiltered(JobFilters filters);
    void deleteJob(UUID jobId, UserEntity initiator);
}
