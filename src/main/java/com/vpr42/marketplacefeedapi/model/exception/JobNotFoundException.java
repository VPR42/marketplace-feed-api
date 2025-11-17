package com.vpr42.marketplacefeedapi.model.exception;

import com.vpr42.marketplacefeedapi.model.enums.ApiError;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class JobNotFoundException extends ApplicationException {
    public JobNotFoundException(UUID id) {
        super(
                "Job with id %s is not found".formatted(id),
                ApiError.JOB_NOT_FOUND,
                HttpStatus.NOT_FOUND
        );
    }
}
