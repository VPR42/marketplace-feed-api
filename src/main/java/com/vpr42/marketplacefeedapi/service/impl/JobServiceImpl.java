package com.vpr42.marketplacefeedapi.service.impl;

import com.vpr42.marketplacefeedapi.mappers.ServiceMapper;
import com.vpr42.marketplacefeedapi.model.dto.CreateJobDto;
import com.vpr42.marketplacefeedapi.model.dto.Job;
import com.vpr42.marketplacefeedapi.model.entity.CategoryEntity;
import com.vpr42.marketplacefeedapi.model.entity.JobEntity;
import com.vpr42.marketplacefeedapi.model.entity.TagEntity;
import com.vpr42.marketplacefeedapi.model.entity.UserEntity;
import com.vpr42.marketplacefeedapi.model.enums.ApiError;
import com.vpr42.marketplacefeedapi.model.exception.ApplicationException;
import com.vpr42.marketplacefeedapi.model.exception.CategoryNotFoundException;
import com.vpr42.marketplacefeedapi.model.exception.TagsNotFoundException;
import com.vpr42.marketplacefeedapi.repository.CategoryRepository;
import com.vpr42.marketplacefeedapi.repository.JobRepository;
import com.vpr42.marketplacefeedapi.repository.TagsRepository;
import com.vpr42.marketplacefeedapi.service.JobService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
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
        CategoryEntity categoryEntity = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException(dto.categoryId()));

        Set<TagEntity> tags = tagsRepository.findByNameIn(dto.tags());
        if (tags.size() < dto.tags().size()) {
            Set<String> presentedName = tags.stream()
                    .map(TagEntity::getName)
                    .collect(Collectors.toSet());
            // Удаляем тэги, которые есть в БД
            dto.tags().removeAll(presentedName);

            throw new TagsNotFoundException(dto.tags());
        }

        try {
            JobEntity entity = ServiceMapper.toEntity(dto, tags, categoryEntity, initiator);
            JobEntity createdService = jobRepository.save(entity);

            return ServiceMapper.fromEntity(createdService, 0);
        } catch (DataAccessException exception) {
            log.error("An error occurred creating new service for user with id: {}", initiator.getId(), exception);

            throw new ApplicationException("Failed to create new service", ApiError.INVALID_DATA, HttpStatus.BAD_REQUEST);
        }
    }
}
