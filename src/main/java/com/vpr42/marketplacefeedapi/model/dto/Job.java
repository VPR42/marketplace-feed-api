package com.vpr42.marketplacefeedapi.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * Dto с информацией об услуге
 */
public record Job(
    UUID id,
    String name,
    String description,
    BigDecimal price,
    String coverUrl,
    LocalDateTime createdAt,
    User master,
    Category category,
    Set<String> tags,
    Long ordersCount
) {
}
