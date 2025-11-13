package com.vpr42.marketplacefeedapi.service.impl;

import com.vpr42.marketplacefeedapi.mappers.ServiceMapper;
import com.vpr42.marketplacefeedapi.model.dto.CreateServiceDto;
import com.vpr42.marketplacefeedapi.model.dto.Service;
import com.vpr42.marketplacefeedapi.model.entity.CategoryEntity;
import com.vpr42.marketplacefeedapi.model.entity.ServiceEntity;
import com.vpr42.marketplacefeedapi.model.entity.TagEntity;
import com.vpr42.marketplacefeedapi.model.entity.UserEntity;
import com.vpr42.marketplacefeedapi.model.enums.ApiError;
import com.vpr42.marketplacefeedapi.model.exception.ApplicationException;
import com.vpr42.marketplacefeedapi.model.exception.CategoryNotFoundException;
import com.vpr42.marketplacefeedapi.model.exception.TagsNotFoundException;
import com.vpr42.marketplacefeedapi.repository.CategoryRepository;
import com.vpr42.marketplacefeedapi.repository.ServiceRepository;
import com.vpr42.marketplacefeedapi.repository.TagsRepository;
import com.vpr42.marketplacefeedapi.service.ServicesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class ServicesServiceImpl implements ServicesService {
    private final ServiceRepository serviceRepository;
    private final TagsRepository tagsRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Service createService(CreateServiceDto dto, UserEntity initiator) {
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
            ServiceEntity entity = ServiceMapper.toEntity(dto, tags, categoryEntity, initiator);
            ServiceEntity createdService = serviceRepository.save(entity);

            return ServiceMapper.fromEntity(createdService, 0);
        } catch (DataAccessException exception) {
            log.error("An error occurred creating new service for user with id: {}", initiator.getId(), exception);

            throw new ApplicationException("Failed to create new service", ApiError.INVALID_DATA, HttpStatus.BAD_REQUEST);
        }
    }
}
