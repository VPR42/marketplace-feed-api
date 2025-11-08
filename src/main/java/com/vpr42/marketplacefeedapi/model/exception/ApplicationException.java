package com.vpr42.marketplacefeedapi.model.exception;

import com.vpr42.marketplacefeedapi.model.enums.ApiError;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ApplicationException extends RuntimeException {
    private ApiError error;
    private HttpStatus statusCode;

    public ApplicationException(String message,
                                ApiError error,
                                HttpStatus statusCode) {
        super(message);
        this.error = error;
        this.statusCode = statusCode;
    }
}
