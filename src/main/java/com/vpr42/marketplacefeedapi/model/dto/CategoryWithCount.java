package com.vpr42.marketplacefeedapi.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Dto для получения категорий с соотв-м количеством услуг по ней
 * @param category
 * @param count
 */
@Schema(description = "Категория с количеством вакансий")
public record CategoryWithCount(
    Category category,
    long count
) { }
