package com.vpr42.marketplacefeedapi.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Dto для категории
 * @param id Id категории
 * @param name Имя категории
 */
@Schema(description = "Категория")
public record Category(
    Integer id,
    String name
) {
}
