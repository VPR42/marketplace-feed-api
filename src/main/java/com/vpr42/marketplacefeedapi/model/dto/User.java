package com.vpr42.marketplacefeedapi.model.dto;

import com.vpr42.marketplacefeedapi.model.entity.CityEntity;

import java.util.UUID;

public record User(
    UUID id,
    String email,
    String name,
    String surname,
    String patronymic,
    String avatarPath,
    CityEntity city
) { }
