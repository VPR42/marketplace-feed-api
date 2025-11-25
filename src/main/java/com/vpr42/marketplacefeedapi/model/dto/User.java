package com.vpr42.marketplacefeedapi.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

/**
 * Dto пользователя
 * @param id Id
 * @param email Email
 * @param name Имя
 * @param surname Фамилия
 * @param patronymic Отчество
 * @param avatarPath Аватар
 * @param city Город
 * @param master Информация как о мастере
 */
@Schema(description = "Пользователь")
public record User(
    UUID id,
    String email,
    String name,
    String surname,
    String patronymic,
    String avatarPath,
    City city,
    Master master
) { }
