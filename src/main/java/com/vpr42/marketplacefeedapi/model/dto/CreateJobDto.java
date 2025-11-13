package com.vpr42.marketplacefeedapi.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

/**
 * Dto для добавления услуги
 * @param name Название услуги
 * @param description Описание услуги
 * @param price Цена
 * @param coverUrl Url обложки
 * @param categoryId Id категории
 * @param tags Список тэгов в виде строк
 */
public record CreateJobDto(
    @NotNull(message = "Job name can't be null")
    @Size(min = 5, message = "Job name must be at least {min} characters long")
    String name,
    @NotNull(message = "Job description can't be null")
    @Size(min = 20, message = "Job description must be at least {min} characters long")
    String description,
    @DecimalMin(value = "100", message = "Job price ${formatter.format('%1$.2f', validatedValue)} must be higher than {value}")
    BigDecimal price,
    String coverUrl,
    @NotNull(message = "Job must have category")
    Integer categoryId,
    @NotEmpty(message = "Job must have at least 1 tag")
    List<String> tags
) {
}
