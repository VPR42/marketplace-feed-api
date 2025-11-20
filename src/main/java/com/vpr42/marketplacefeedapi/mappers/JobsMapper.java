package com.vpr42.marketplacefeedapi.mappers;

import com.vpr42.marketplacefeedapi.model.dto.CreateJobDto;
import com.vpr42.marketplacefeedapi.model.dto.Job;
import com.vpr42.marketplacefeedapi.model.entity.CategoryEntity;
import com.vpr42.marketplacefeedapi.model.entity.JobEntity;
import com.vpr42.marketplacefeedapi.model.entity.TagEntity;
import com.vpr42.marketplacefeedapi.model.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Маппер для услуг
 */
@Slf4j
public class JobsMapper {
    public static Job fromEntity(JobEntity entity, long orderCount) {
        log.info("Converting ServiceEntity to dto: {}", entity);

        return new Job(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getPrice(),
            entity.getCoverUrl(),
            entity.getCreatedAt(),
            UserMapper.fromEntity(entity.getMasterInfo().getUser()),
            CategoryMapper.fromEntity(entity.getCategory()),
            entity.getTags()
                    .stream()
                    .map(TagEntity::getName)
                    .collect(Collectors.toSet()),
            orderCount
        );
    }

    public static JobEntity toEntity(CreateJobDto dto,
                                     Set<TagEntity> tags,
                                     CategoryEntity category,
                                     UserEntity user) {
        log.info("Converting CreateServiceDto to entity: {} for user with id: {}", dto, user.getId());

        return JobEntity.builder()
                .name(dto.name())
                .description(dto.description())
                .price(dto.price())
                .coverUrl(dto.coverUrl())
                .category(category)
                .masterInfo(user.getMasterInfo())
                .tags(tags)
                .build();
    }
}
