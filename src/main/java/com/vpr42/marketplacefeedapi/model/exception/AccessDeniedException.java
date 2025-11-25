package com.vpr42.marketplacefeedapi.model.exception;

import com.vpr42.marketplacefeedapi.model.enums.ApiError;
import org.springframework.http.HttpStatus;

public class AccessDeniedException extends ApplicationException {
    public AccessDeniedException(String message) {
        super(
                message,
                ApiError.ACCESS_DENIED,
                HttpStatus.FORBIDDEN
        );
    }

    public AccessDeniedException() {
        super(
                "Access denied",
                ApiError.ACCESS_DENIED,
                HttpStatus.FORBIDDEN
        );
    }
}