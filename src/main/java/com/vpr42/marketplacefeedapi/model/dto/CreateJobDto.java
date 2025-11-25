package com.vpr42.marketplacefeedapi.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

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
@Schema(description = "Данные для создания вакансии")
public record CreateJobDto(
    @NotNull(message = "Job name can't be null")
    @Size(min = 5, message = "Job name must be at least {min} characters long")
    @Schema(description = "Имя вакансии (Не NULL)", minLength = 5)
    String name,

    @NotNull(message = "Job description can't be null")
    @Size(min = 20, message = "Job description must be at least {min} characters long")
    @Schema(description = "Описание вакансии (Не NULL)", minLength = 20)
    String description,

    @NotNull(message = "Job price can't be null")
    @DecimalMin(value = "100", message = "Job price ${formatter.format('%1$.2f', validatedValue)} must be higher than {value}")
    @Schema(description = "Цена (от 100 до 999999)", minimum = "100")
    BigDecimal price,

    @URL(message = "Cover must be a valid url")
    @Schema(description = "Url обложки / NULL", nullable = true)
    String coverUrl,

    @NotNull(message = "Job must have category")
    @Schema(description = "Id категории")
    Integer categoryId,

    @NotEmpty(message = "Job must have at least 1 tag")
    @Schema(description = "Список тэгов", minLength = 1)
    List<String> tags
) {
}
