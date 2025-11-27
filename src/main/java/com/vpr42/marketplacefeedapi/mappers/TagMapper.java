package com.vpr42.marketplacefeedapi.mappers;

import com.vpr42.marketplacefeedapi.model.dto.TagDto;
import com.vpr42.marketplacefeedapi.model.entity.TagEntity;

import java.util.List;

public class TagMapper {

    public static TagDto fromEntity(TagEntity entity) {
        return new TagDto(
                entity.getId(),
                entity.getName()
        );
    }

    public static List<TagDto> fromEntities(List<TagEntity> entities) {
        return entities.stream()
                .map(TagMapper::fromEntity)
                .toList();
    }
}
