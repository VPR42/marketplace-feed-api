package com.vpr42.marketplacefeedapi.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Schema(description = "Dto для обновления вакансии")
public record UpdateJobDto(
        @NotNull(message = "Id can't be null")
        UUID id,

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

        @NotNull(message = "Job must have category")
        @Schema(description = "Id категории")
        Integer categoryId,

        @NotEmpty(message = "Job must have at least 1 tag")
        @Schema(description = "Список тэгов", minLength = 1)
        List<Integer> tags) {
}
