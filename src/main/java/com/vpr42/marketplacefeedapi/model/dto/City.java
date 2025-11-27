package com.vpr42.marketplacefeedapi.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Dto с информацией о городе
 * @param id Id
 * @param region Имя региона
 * @param name Имя города
 */
@Schema(description = "Город")
public record City(
    Integer id,
    String region,
    String name
) {
}
