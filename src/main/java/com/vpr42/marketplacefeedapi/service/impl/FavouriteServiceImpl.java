package com.vpr42.marketplacefeedapi.service.impl;

import com.vpr42.marketplacefeedapi.mappers.JobsMapper;
import com.vpr42.marketplacefeedapi.model.dto.AddToFavouriteDto;
import com.vpr42.marketplacefeedapi.model.dto.FavouriteFilters;
import com.vpr42.marketplacefeedapi.model.dto.Job;
import com.vpr42.marketplacefeedapi.model.dto.JobEntityWithCount;
import com.vpr42.marketplacefeedapi.model.entity.FavouriteJobEntity;
import com.vpr42.marketplacefeedapi.model.entity.JobEntity;
import com.vpr42.marketplacefeedapi.model.entity.UserEntity;
import com.vpr42.marketplacefeedapi.model.entity.keys.FavouriteKey;
import com.vpr42.marketplacefeedapi.model.exception.JobNotFoundException;
import com.vpr42.marketplacefeedapi.model.exception.SelfFavouriteException;
import com.vpr42.marketplacefeedapi.repository.FavouriteJobRepository;
import com.vpr42.marketplacefeedapi.repository.JobRepository;
import com.vpr42.marketplacefeedapi.repository.criteria.FavouriteFilteringSpecification;
import com.vpr42.marketplacefeedapi.service.FavouriteService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavouriteServiceImpl implements FavouriteService {
    private final FavouriteJobRepository favouriteJobRepository;
    private final JobRepository jobRepository;

    @Override
    @Transactional
    public void addToFavourite(AddToFavouriteDto dto, UserEntity user) {
        if (isJobInFavourite(dto.jobId(), user)) {
            log.info("Job {} is already in favourites for user {}", dto.jobId(), user.getId());

            return;
        }

        JobEntity job = jobRepository.findById(dto.jobId())
                .orElseThrow(() -> new JobNotFoundException(dto.jobId()));

        // Проверяем, что пользователь не пытается добавить свою же услугу
        if (isUserJobOwner(job, user)) {
            log.warn("User {} attempted to add their own job {} to favourites", user.getId(), dto.jobId());

            throw new SelfFavouriteException();
        }

        FavouriteKey key = new FavouriteKey(user.getId(), dto.jobId());
        FavouriteJobEntity favourite = FavouriteJobEntity.builder()
                .key(key)
                .user(user)
                .service(job)
                .build();

        favouriteJobRepository.save(favourite);
        log.info("Job {} added to favourites for user {}", dto.jobId(), user.getId());
    }

    @Override
    @Transactional
    public void removeFromFavourite(UUID jobId, UserEntity user) {
        FavouriteKey key = new FavouriteKey(user.getId(), jobId);
        favouriteJobRepository.deleteById(key);
        log.info("Job {} removed from favourites for user {}", jobId, user.getId());
    }

    @Override
    @Transactional
    public Page<Job> getFavouriteJobs(FavouriteFilters filters, UserEntity user) {
        log.info("Fetching favourite jobs for user {} with filters: {}", user.getId(), filters);

        Specification<JobEntity> criteriaSpec = FavouriteFilteringSpecification.filter(filters, user.getId());
        Pageable pageable = PageRequest.of(filters.getPage(), filters.getPageSize());

        Page<JobEntity> favouriteJobsPage = jobRepository.findAll(criteriaSpec, pageable);

        if (favouriteJobsPage.isEmpty()) {
            log.info("No favourite jobs found for user {} with given filters", user.getId());
            return Page.empty(pageable);
        }

        List<UUID> jobIds = favouriteJobsPage.getContent().stream()
                .map(JobEntity::getId)
                .toList();

        List<JobEntity> jobs = jobRepository.findAllEntitiesWithIds(jobIds);
        Map<UUID, JobEntity> jobsById = jobs.stream()
                .collect(Collectors.toMap(JobEntity::getId, v -> v));

        List<JobEntityWithCount> jobCounts = jobRepository.findOrdersCountFor(jobIds);
        Map<UUID, Long> jobsCount = jobCounts.stream()
                .collect(Collectors.toMap(k -> k.job().getId(), JobEntityWithCount::count));

        List<Job> jobDtos = favouriteJobsPage.getContent().stream()
                .map(jobEntity -> {
                    JobEntity fullJobEntity = jobsById.get(jobEntity.getId());
                    Long ordersCount = jobsCount.getOrDefault(jobEntity.getId(), 0L);
                    return JobsMapper.fromEntity(fullJobEntity, ordersCount.intValue());
                })
                .toList();

        log.info("Found {} favorite jobs for user {}", jobDtos.size(), user.getId());
        return new PageImpl<>(jobDtos, pageable, favouriteJobsPage.getTotalElements());
    }

    @Override
    @Transactional
    public boolean isJobInFavourite(UUID jobId, UserEntity user) {
        FavouriteKey key = new FavouriteKey(user.getId(), jobId);

        return favouriteJobRepository.existsById(key);
    }

    /**
     * Проверяет, является ли пользователь владельцем услуги
     * @param job услуга для проверки
     * @param user пользователь
     * @return true если пользователь владелец услуги, иначе false
     */
    private boolean isUserJobOwner(JobEntity job, UserEntity user) {
        return job.getMasterInfo() != null &&
                job.getMasterInfo().getUser() != null &&
                job.getMasterInfo().getUser().getId().equals(user.getId());
    }
}
