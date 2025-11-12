package com.vpr42.marketplacefeedapi.model.dto;

import com.vpr42.marketplacefeedapi.model.enums.ApiError;

import java.util.List;
import java.util.Map;

public record ApiErrorResponse(
    ApiError errorCode,
    String message,
    Map<String,
    List<String>> errors
) {
    public ApiErrorResponse(ApiError errorCode, String message) {
        this(errorCode, message, Map.of());
    }
}
