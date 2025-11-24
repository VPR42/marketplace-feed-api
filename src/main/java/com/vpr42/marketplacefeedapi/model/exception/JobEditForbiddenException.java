package com.vpr42.marketplacefeedapi.model.exception;

import com.vpr42.marketplacefeedapi.model.enums.ApiError;
import com.vpr42.marketplacefeedapi.model.exception.ApplicationException;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class JobEditForbiddenException extends ApplicationException {

    public JobEditForbiddenException(UUID jobId, UUID userId) {
        super(
                "User " + userId + " is not allowed to edit job " + jobId,
                ApiError.FORBIDDEN,
                HttpStatus.FORBIDDEN
        );
    }
}
