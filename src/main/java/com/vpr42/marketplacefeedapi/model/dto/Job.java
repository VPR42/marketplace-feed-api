package com.vpr42.marketplacefeedapi.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * Dto с информацией об услуге
 */
@Schema(description = "Услуга")
public record Job(
    UUID id,
    String name,
    String description,
    BigDecimal price,
    @Schema(description = "Ссылка на обложку")
    String coverUrl,

    LocalDateTime createdAt,
    User master,
    Category category,
    Set<String> tags,

    @Schema(description = "Число заказов услуги")
    Long ordersCount
) {
}
