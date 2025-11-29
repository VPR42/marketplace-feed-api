package com.vpr42.marketplacefeedapi.service;

import com.vpr42.marketplacefeedapi.model.dto.CoverUploadResponse;
import com.vpr42.marketplacefeedapi.model.dto.CreateJobDto;
import com.vpr42.marketplacefeedapi.model.dto.Job;
import com.vpr42.marketplacefeedapi.model.dto.JobFilters;
import com.vpr42.marketplacefeedapi.model.dto.UpdateJobDto;
import com.vpr42.marketplacefeedapi.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface JobService {
    Job createJob(CreateJobDto dto, UserEntity initiator);
    Page<Job> getJobsFiltered(JobFilters filters, UUID initiatorId);
    void deleteJob(UUID jobId, UserEntity initiator);
    Job getJobById(UUID id);
    Job updateJob(UpdateJobDto dto, UserEntity initiator);
    CoverUploadResponse updateCover(UUID jobId, MultipartFile file);
}
