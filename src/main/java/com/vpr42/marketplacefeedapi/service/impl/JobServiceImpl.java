package com.vpr42.marketplacefeedapi.service.impl;

import com.vpr42.marketplacefeedapi.mappers.JobsMapper;
import com.vpr42.marketplacefeedapi.model.dto.CreateJobDto;
import com.vpr42.marketplacefeedapi.model.dto.Job;
import com.vpr42.marketplacefeedapi.model.dto.JobEntityWithCount;
import com.vpr42.marketplacefeedapi.model.dto.JobFilters;
import com.vpr42.marketplacefeedapi.model.entity.CategoryEntity;
import com.vpr42.marketplacefeedapi.model.entity.JobEntity;
import com.vpr42.marketplacefeedapi.model.entity.TagEntity;
import com.vpr42.marketplacefeedapi.model.entity.UserEntity;
import com.vpr42.marketplacefeedapi.model.enums.ApiError;
import com.vpr42.marketplacefeedapi.model.exception.ApplicationException;
import com.vpr42.marketplacefeedapi.model.exception.CategoryNotFoundException;
import com.vpr42.marketplacefeedapi.model.exception.JobAlreadyExistsForUser;
import com.vpr42.marketplacefeedapi.model.exception.JobsNotFoundException;
import com.vpr42.marketplacefeedapi.model.exception.TagsNotFoundException;
import com.vpr42.marketplacefeedapi.repository.CategoryRepository;
import com.vpr42.marketplacefeedapi.repository.JobRepository;
import com.vpr42.marketplacefeedapi.repository.TagsRepository;
import com.vpr42.marketplacefeedapi.repository.criteria.JobFilteringSpecification;
import com.vpr42.marketplacefeedapi.service.JobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {
    private final JobRepository jobRepository;
    private final TagsRepository tagsRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Job createJob(CreateJobDto dto, UserEntity initiator) {
        Optional<JobEntity> oldJob = jobRepository.findByMasterIdAndName(initiator.getId(), dto.name());

        if (oldJob.isPresent())
            throw new JobAlreadyExistsForUser(dto.name());

        CategoryEntity categoryEntity = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException(dto.categoryId()));
        log.info("Fetched category: {} for createJob for user: {}", categoryEntity.getName(), initiator.getId());

        Set<TagEntity> tags = tagsRepository.findByNameIn(dto.tags());
        log.info("Fetched tags: {} for createJob for user: {}", tags, initiator.getId());
        if (tags.size() < dto.tags().size()) {
            Set<String> presentedName = tags.stream()
                    .map(TagEntity::getName)
                    .collect(Collectors.toSet());
            // Удаляем тэги, которые есть в БД
            dto.tags().removeAll(presentedName);
            log.info("Given tags are absent: {}", dto.tags());

            throw new TagsNotFoundException(dto.tags());
        }

        try {
            JobEntity entity = JobsMapper.toEntity(dto, tags, categoryEntity, initiator);
            JobEntity createdService = jobRepository.save(entity);

            return JobsMapper.fromEntity(createdService, 0);
        } catch (DataAccessException exception) {
            log.error("An error occurred creating new service for user with id: {}", initiator.getId(), exception);

            throw new ApplicationException("Failed to create new service", ApiError.INVALID_DATA, HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @Transactional
    public Page<Job> getJobsFiltered(JobFilters filters) {
        Specification<JobEntity> criteriaSpec = JobFilteringSpecification.filter(filters);
        Pageable pageable = PageRequest.of(filters.getPage(), filters.getPageSize());
        Page<JobEntity> jobIdsFiltered = jobRepository.findAll(criteriaSpec, pageable);
        log.info("Fetched {} jobs by given filters", jobIdsFiltered.getSize());

        if (jobIdsFiltered.isEmpty()) {
            throw new JobsNotFoundException();
        }

        List<UUID> ids = jobIdsFiltered.stream()
                .map(JobEntity::getId)
                .toList();
        log.info("ID's of entities that satisfies given filters: {}", ids);
        log.info("Fetching jobs with given ids");
        List<JobEntity> jobs = jobRepository.findAllEntitiesWithIds(ids);
        Map<UUID, JobEntity> jobsById = jobs.stream()
                .collect(Collectors.toMap(JobEntity::getId, v -> v));

        log.info("Fetching count of orders for given ids");
        List<JobEntityWithCount> jobCounts = jobRepository.findOrdersCountFor(ids);
        Map<UUID, Long> jobsCount = jobCounts.stream()
                .collect(Collectors.toMap(k -> k.job().getId(), JobEntityWithCount::count));

        return jobIdsFiltered
                .map(el -> JobsMapper.fromEntity(jobsById.get(el.getId()), jobsCount.get(el.getId())));
    }

    @Override
    @Transactional
    public void deleteJob(UUID jobId, UserEntity initiator) {
        // Проверка существования услуги
        JobEntity job = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobsNotFoundException("Job not found with id: " + jobId));

        // Проверка прав доступа - только владелец может удалять свою услугу
        if (!job.getMasterInfo().getUser().getId().equals(initiator.getId())) {
            throw new ApplicationException("You can only delete your own jobs",
                    ApiError.ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }

        try {

            job.getTags().clear(); // Очищаем связи с тегами перед удалением

            // Удаление основной сущности
            jobRepository.delete(job);
            log.info("Job with id: {} successfully deleted by user: {}", jobId, initiator.getId());

        } catch (DataAccessException exception) {
            log.error("An error occurred deleting job with id: {} for user: {}", jobId, initiator.getId(), exception);
            throw new ApplicationException("Failed to delete job", ApiError.DATABASE_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
