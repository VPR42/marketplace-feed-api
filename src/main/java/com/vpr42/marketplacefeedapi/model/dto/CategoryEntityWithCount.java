package com.vpr42.marketplacefeedapi.model.dto;

import com.vpr42.marketplacefeedapi.model.entity.CategoryEntity;

public record CategoryEntityWithCount(
    CategoryEntity category,
    long count
) { }
