package com.vpr42.marketplacefeedapi.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Set;

/**
 * Dto с информацией о мастере
 * @param experience Опыт
 * @param description Описание
 * @param pseudonym Псевдоним
 * @param phoneNumber Номер телефона
 * @param skills Список навыков
 * @param daysOfWeek Дни недели
 * @param startTime Начало рабочего дня
 * @param endTime Конец рабочего дня
 */
@Schema(description = "Мастер")
public record Master(
    Integer experience,
    String description,
    String pseudonym,
    String phoneNumber,
    @Schema(description = "Список навыков")
    Set<String> skills,
    int[] daysOfWeek,
    String startTime,
    String endTime
) { }
