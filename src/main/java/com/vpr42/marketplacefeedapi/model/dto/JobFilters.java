package com.vpr42.marketplacefeedapi.model.dto;

import com.vpr42.marketplacefeedapi.model.enums.SortType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
@Schema(description = "Список фильтров для получения вакансий")
public class JobFilters {

    @NotNull(message = "Page number can't be null")
    @Min(value = 0, message = "Page number can't be less then 0")
    @Schema(description = "Номер страницы", minimum = "0")
    private Integer page = 0;

    @NotNull(message = "Page size can't be null")
    @Min(value = 1, message = "Page must contain elements")
    @Schema(description = "Размер страницы", minimum = "1", defaultValue = "15")
    private Integer pageSize = 15;

    @Schema(description = "Id мастера / NULL", nullable = true)
    private UUID masterId;

    @Schema(description = "Поисковый запрос / NULL. Работает по имени услуги + псевдониму мастера",
        nullable = true)
    private String query;

    @Min(value = 1, message = "Category id must be greater than 0")
    @Schema(description = "Id категории / NULL", nullable = true)
    private Integer categoryId;

    @Schema(description = "Список навыков мастера / NULL", nullable = true)
    private String[] skills;

    @Schema(description = "Список тэгов услуги / NULL", nullable = true)
    private String[] tags;

    @Min(value = 0, message = "MinPrice must be positive or zero value")
    @Schema(description = "Минимальная цена / NULL", minimum = "0", nullable = true)
    private Integer minPrice;

    @Min(value = 1, message = "MaxPrice must be positive")
    @Schema(description = "Максимальная цена / NULL. Если максимальная цена <= минимальной, то параметр игнорируется",
            maximum = "999999", nullable = true)
    private Integer maxPrice;

    @Min(value = 0, message = "Experience must be positive or zero value")
    @Schema(description = "Минимальный опыт мастера в годах/NULL", minimum = "0", nullable = true)
    private Integer experience;

    @Min(value = 1, message = "City id must be greater than 1")
    @Schema(description = "Id города/NULL", minimum = "1", nullable = true)
    private Integer cityId;

    @Min(value = 0, message = "MinOrders must be positive or zero value")
    @Schema(description = "Минимальное число заказов/NULL", minimum = "0", nullable = true)
    private Integer minOrders;

    @Schema(description = "Сортировка по цене/NULL",
            allowableValues = {"ASC", "DESC"},
            nullable = true)
    private SortType priceSort;

    @Schema(description = "Сортировка по количеству заказов/NULL",
            allowableValues = {"ASC", "DESC"},
            nullable = true)
    private SortType ordersCountSort;

    @Schema(description = "Сортировка по опыту мастера/NULL",
            allowableValues = {"ASC", "DESC"},
            nullable = true)
    private SortType experienceSort;
}
