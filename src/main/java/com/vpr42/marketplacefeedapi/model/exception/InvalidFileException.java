package com.vpr42.marketplacefeedapi.model.exception;

import com.vpr42.marketplacefeedapi.model.enums.ApiError;
import org.springframework.http.HttpStatus;

public class InvalidFileException extends ApplicationException {
    public InvalidFileException() {
        super(
            "Unable to load file to server",
            ApiError.INVALID_FILE,
            HttpStatus.BAD_REQUEST
        );
    }
}
