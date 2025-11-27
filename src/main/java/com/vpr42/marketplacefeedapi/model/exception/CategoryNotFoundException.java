package com.vpr42.marketplacefeedapi.model.exception;

import com.vpr42.marketplacefeedapi.model.enums.ApiError;
import org.springframework.http.HttpStatus;

public class CategoryNotFoundException extends ApplicationException {
    public CategoryNotFoundException(Integer id) {
        super(
            "Category with id %d is not found".formatted(id),
            ApiError.CATEGORY_NOT_FOUND,
            HttpStatus.NOT_FOUND
        );
    }
}
