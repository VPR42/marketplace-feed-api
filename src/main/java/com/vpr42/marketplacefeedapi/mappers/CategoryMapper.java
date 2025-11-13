package com.vpr42.marketplacefeedapi.mappers;

import com.vpr42.marketplacefeedapi.model.dto.Category;
import com.vpr42.marketplacefeedapi.model.entity.CategoryEntity;
import lombok.extern.slf4j.Slf4j;

/**
 * Маппер для категорий
 */
@Slf4j
public class CategoryMapper {
    public static Category fromEntity(CategoryEntity entity) {
        log.info("Converting CategoryEntity to dto: {}", entity);
        return new Category(
                entity.getId(),
                entity.getName()
        );
    }
}
