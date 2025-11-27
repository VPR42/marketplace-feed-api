package com.vpr42.marketplacefeedapi.model.dto;

import com.vpr42.marketplacefeedapi.model.entity.JobEntity;

/**
 * Используется для получения списка вакансий с подсчетом в JPA
 * @param job
 * @param count
 */
public record JobEntityWithCount(
    JobEntity job,
    Long count
) {

}