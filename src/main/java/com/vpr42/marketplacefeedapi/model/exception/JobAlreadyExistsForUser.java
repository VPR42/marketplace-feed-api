package com.vpr42.marketplacefeedapi.model.exception;

import com.vpr42.marketplacefeedapi.model.enums.ApiError;
import org.springframework.http.HttpStatus;

public class JobAlreadyExistsForUser extends ApplicationException {
    public JobAlreadyExistsForUser(String name) {
        super(
            "Job with name %s already created by user".formatted(name),
            ApiError.JOB_ALREADY_EXISTS,
            HttpStatus.CONFLICT
        );
    }
}
