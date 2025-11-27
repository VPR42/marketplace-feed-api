package com.vpr42.marketplacefeedapi.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

/**
 * Dto с информацией о мастере
 * @param experience Опыт
 * @param description Описание
 * @param pseudonym Псевдоним
 * @param phoneNumber Номер телефона
 * @param workingHours Рабочие часы
 * @param skills Список навыков
 */
@Schema(description = "Мастер")
public record Master(
    Integer experience,
    String description,
    String pseudonym,
    String phoneNumber,
    @Schema(description = "Рабочие часы в виде 9:00-20:00")
    String workingHours,
    @Schema(description = "Список навыков")
    Set<String> skills
) { }
