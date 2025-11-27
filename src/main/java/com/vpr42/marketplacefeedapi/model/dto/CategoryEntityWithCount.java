package com.vpr42.marketplacefeedapi.model.dto;

import com.vpr42.marketplacefeedapi.model.entity.CategoryEntity;

/**
 * Используется для получения категорий с количеством вакансий из JPA
 * @param category Категория (В виде сущности)
 * @param count Количество услуг по данной категории
 */
public record CategoryEntityWithCount(
    CategoryEntity category,
    long count
) { }
