package com.vpr42.marketplacefeedapi.model.dto;

import java.util.UUID;

/**
 * Dto пользователя
 */
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
