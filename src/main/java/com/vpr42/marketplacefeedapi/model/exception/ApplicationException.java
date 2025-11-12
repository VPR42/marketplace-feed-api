package com.vpr42.marketplacefeedapi.model.exception;

import com.vpr42.marketplacefeedapi.model.enums.ApiError;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApplicationException extends RuntimeException {
    private final ApiError error;
    private final HttpStatus statusCode;

    public ApplicationException(String message,
                                ApiError error,
                                HttpStatus statusCode) {
        super(message);
        this.error = error;
        this.statusCode = statusCode;
    }
}
