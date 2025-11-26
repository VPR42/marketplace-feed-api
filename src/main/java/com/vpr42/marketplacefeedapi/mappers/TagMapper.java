package com.vpr42.marketplacefeedapi.mappers;

import com.vpr42.marketplacefeedapi.model.dto.TagDto;
import com.vpr42.marketplacefeedapi.model.entity.TagEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TagMapper {

    public TagDto fromEntity(TagEntity entity) {
        return new TagDto(
                entity.getId(),
                entity.getName()
        );
    }

    public List<TagDto> fromEntities(List<TagEntity> entities) {
        return entities.stream()
                .map(this::fromEntity)
                .toList();
    }
}