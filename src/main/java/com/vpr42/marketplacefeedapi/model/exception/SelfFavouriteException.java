package com.vpr42.marketplacefeedapi.model.exception;

import com.vpr42.marketplacefeedapi.model.enums.ApiError;
import org.springframework.http.HttpStatus;

public class SelfFavouriteException extends ApplicationException {
    public SelfFavouriteException() {
        super(
                "Attempted to add your own job to favourites",
                ApiError.SELF_FAVOURITE,
                HttpStatus.BAD_REQUEST
        );
    }
}
