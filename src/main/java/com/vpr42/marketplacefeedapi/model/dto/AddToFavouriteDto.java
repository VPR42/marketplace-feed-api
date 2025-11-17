package com.vpr42.marketplacefeedapi.model.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * DTO для добавления услуги в избранное
 * @param jobId ID услуги
 */
public record AddToFavouriteDto(
    @NotNull(message = "Job ID can't bu null")
    UUID jobId
) {}
