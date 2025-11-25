package com.vpr42.marketplacefeedapi.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * DTO для добавления услуги в избранное
 * @param jobId ID услуги
 */
@Schema(description = "Данные для добавления в избранное")
public record AddToFavouriteDto(
    @NotNull(message = "Job ID can't bu null")
    UUID jobId
) {}
