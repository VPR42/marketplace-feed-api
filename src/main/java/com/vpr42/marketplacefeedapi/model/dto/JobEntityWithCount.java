package com.vpr42.marketplacefeedapi.model.dto;

import com.vpr42.marketplacefeedapi.model.entity.JobEntity;

public record JobEntityWithCount(
    JobEntity job,
    Long count
) {

}