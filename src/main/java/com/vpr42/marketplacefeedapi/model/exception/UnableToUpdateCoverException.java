package com.vpr42.marketplacefeedapi.model.exception;

import com.vpr42.marketplacefeedapi.model.enums.ApiError;
import org.springframework.http.HttpStatus;

import java.util.UUID;

public class UnableToUpdateCoverException extends ApplicationException {
    public UnableToUpdateCoverException(UUID jobId) {
        super(
            "Unable to update cover for job: " + jobId,
            ApiError.UNABLE_TO_UPDATE_COVER,
            HttpStatus.BAD_REQUEST
        );
    }
}
