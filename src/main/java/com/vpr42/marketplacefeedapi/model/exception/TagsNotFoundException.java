package com.vpr42.marketplacefeedapi.model.exception;

import com.vpr42.marketplacefeedapi.model.enums.ApiError;
import org.springframework.http.HttpStatus;

import java.util.Set;

public class TagsNotFoundException extends ApplicationException {

    public TagsNotFoundException(Set<Integer> missingIds) {
        super(
                "Given tags with ids " + missingIds + " are absent",
                ApiError.TAGS_NOT_FOUND,
                HttpStatus.NOT_FOUND
        );
    }
}
