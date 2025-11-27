package com.vpr42.marketplacefeedapi.model.exception;

import com.vpr42.marketplacefeedapi.model.enums.ApiError;
import org.springframework.http.HttpStatus;

public class JobsNotFoundException extends ApplicationException {
    public JobsNotFoundException() {
        super(
            "Jobs with given filters are not found",
            ApiError.JOBS_NOT_FOUND,
            HttpStatus.NOT_FOUND
        );
    }
}
