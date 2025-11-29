package com.vpr42.marketplacefeedapi.service.impl;

import com.vpr42.marketplacefeedapi.config.properties.CoverProperties;
import com.vpr42.marketplacefeedapi.config.properties.MinioProperties;
import com.vpr42.marketplacefeedapi.mappers.JobsMapper;
import com.vpr42.marketplacefeedapi.model.dto.CoverUploadResponse;
import com.vpr42.marketplacefeedapi.model.dto.CreateJobDto;
import com.vpr42.marketplacefeedapi.model.dto.Job;
import com.vpr42.marketplacefeedapi.model.dto.JobEntityWithCount;
import com.vpr42.marketplacefeedapi.model.dto.JobFilters;
import com.vpr42.marketplacefeedapi.model.dto.UpdateJobDto;
import com.vpr42.marketplacefeedapi.model.entity.CategoryEntity;
import com.vpr42.marketplacefeedapi.model.entity.JobEntity;
import com.vpr42.marketplacefeedapi.model.entity.TagEntity;
import com.vpr42.marketplacefeedapi.model.entity.UserEntity;
import com.vpr42.marketplacefeedapi.model.exception.CategoryNotFoundException;
import com.vpr42.marketplacefeedapi.model.exception.InvalidFileException;
import com.vpr42.marketplacefeedapi.model.exception.JobAlreadyExistsForUser;
import com.vpr42.marketplacefeedapi.model.exception.JobNotFoundException;
import com.vpr42.marketplacefeedapi.model.exception.JobsNotFoundException;
import com.vpr42.marketplacefeedapi.model.exception.TagsNotFoundException;
import com.vpr42.marketplacefeedapi.model.exception.UnableToUpdateCoverException;
import com.vpr42.marketplacefeedapi.repository.CategoryRepository;
import com.vpr42.marketplacefeedapi.repository.JobRepository;
import com.vpr42.marketplacefeedapi.repository.OrderRepository;
import com.vpr42.marketplacefeedapi.repository.TagsRepository;
import com.vpr42.marketplacefeedapi.repository.criteria.JobFilteringSpecification;
import com.vpr42.marketplacefeedapi.service.JobService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final OrderRepository orderRepository;
    private final MinioClient minioClient;

    private final CoverProperties coverProperties;
    private final MinioProperties minioProperties;

    @Override
    @Transactional
    public Job createJob(CreateJobDto dto, UserEntity initiator) {
        Optional<JobEntity> oldJob = jobRepository.findByMasterIdAndName(initiator.getId(), dto.name());

        if (oldJob.isPresent()) {
            throw new JobAlreadyExistsForUser(dto.name());
        }

        CategoryEntity categoryEntity = categoryRepository
                .findById(dto.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException(dto.categoryId()));

        log.info("Fetched category: {} for createJob for user: {}", categoryEntity.getName(), initiator.getId());

        Set<TagEntity> tags = getTagsOrThrow(dto.tags());

        log.info("Fetched tags: {} for createJob for user: {}", tags, initiator.getId());

        JobEntity entity = JobsMapper.toEntity(dto, tags, categoryEntity, initiator);
        JobEntity createdService = jobRepository.save(entity);

        return JobsMapper.fromEntity(createdService, 0);
    }

    @Override
    @Transactional
    public Page<Job> getJobsFiltered(JobFilters filters, UUID initiatorId) {
        Specification<JobEntity> criteriaSpec = JobFilteringSpecification.filter(filters, initiatorId);
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
        JobEntity job = jobRepository.findById(jobId)
                .orElseThrow(JobsNotFoundException::new);

        jobRepository.delete(job);
        log.info("Job with id: {} successfully deleted by user: {}", jobId, initiator.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public Job getJobById(UUID id) {
        log.info("Fetching job by id: {}", id);

        JobEntity entity = jobRepository
                .findWithDetailsById(id)
                .orElseThrow(() -> new JobNotFoundException(id));

        int orderCount = orderRepository.countByJobId(id);

        return JobsMapper.fromEntity(entity, orderCount);
    }

    @Override
    @Transactional
    public Job updateJob(UpdateJobDto dto, UserEntity initiator) {
        log.info("Updating job with id: {} by user: {}", dto.id() , initiator.getId());

        JobEntity entity = jobRepository.findWithDetailsById(dto.id())
                .orElseThrow(() -> new JobNotFoundException(dto.id()));

        if (jobRepository.findByMasterIdAndName(initiator.getId(), dto.name()).isPresent()) {
            throw new JobAlreadyExistsForUser(dto.name());
        }

        CategoryEntity categoryEntity = categoryRepository
                .findById(dto.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException(dto.categoryId()));

        log.info("Fetched category: {} for updateJob for user: {}", categoryEntity.getName(), initiator.getId());

        Set<TagEntity> tags = getTagsOrThrow(dto.tags());
        log.info("Fetched tags: {} for updateJob for user: {}", tags, initiator.getId());

        entity.setName(dto.name());
        entity.setDescription(dto.description());
        entity.setPrice(dto.price());
        entity.setCoverUrl(dto.coverUrl());
        entity.setCategory(categoryEntity);
        entity.setTags(tags);

        int orderCount = orderRepository.countByJobId(dto.id());

        return JobsMapper.fromEntity(entity, orderCount);
    }

    @Override
    @Transactional
    public CoverUploadResponse updateCover(UUID jobId, MultipartFile file) {
        JobEntity job = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException(jobId));

        if (file.getOriginalFilename() == null)
            throw new InvalidFileException();

        String extension = extractFileExtension(file.getOriginalFilename());
        if (!isValidCover(file, extension))
            throw new InvalidFileException();

        if (job.getCoverUrl() != null && job.getCoverUrl().startsWith(minioProperties.urls().publicUrl())) {
            deleteOldCover(jobId, job);
        }
        String savedName = "%s-%s".formatted(
            jobId.toString(),
            file.getOriginalFilename()
        );
        String urlToFile = minioProperties.urls().publicUrl() + savedName;
        log.info("Url for new uploaded file: {}", urlToFile);

        loadCover(jobId, file);
        job.setCoverUrl(urlToFile);

        return new CoverUploadResponse(savedName, urlToFile);
    }

    private void loadCover(UUID jobId, MultipartFile file) {
        try {
            minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(minioProperties.bucket())
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
            );

            log.info("User successfully loaded new cover for job: {}", jobId);
        } catch (Exception ex) {
            log.error("Unable to load cover for file: {}", jobId, ex);

            throw new UnableToUpdateCoverException(jobId);
        }
    }

    private void deleteOldCover(UUID jobId, JobEntity job) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(minioProperties.bucket())
                    .object(job.getCoverUrl())
                    .build()
            );

            log.info("User deleted old cover for job with id: {}", jobId);
        } catch (Exception ex) {
            log.error("Unable to delete cover from minio for job: {}", jobId, ex);

            throw new UnableToUpdateCoverException(jobId);
        }
    }

    private static String extractFileExtension(String fileName) {
        int pointIndex = fileName.lastIndexOf('.');
        if (pointIndex == -1 || pointIndex == fileName.length() - 1)
            throw new InvalidFileException();

        return fileName.substring(pointIndex);
    }

    private boolean isValidCover(MultipartFile file, String extension) {
        return file.getSize() > 0 && coverProperties.allowedExtensions().contains(extension);
    }

    private Set<TagEntity> getTagsOrThrow(List<Integer> tagsIds) {
        Set<TagEntity> tags = tagsRepository.findByIdIn(tagsIds);

        if (tags.size() < tagsIds.size()) {
            Set<Integer> foundIds = tags.stream()
                    .map(TagEntity::getId)
                    .collect(Collectors.toSet());

            Set<Integer> missing = tagsIds.stream()
                    .filter(id -> !foundIds.contains(id))
                    .collect(Collectors.toSet());

            throw new TagsNotFoundException(missing);
        }

        return tags;
    }

}
