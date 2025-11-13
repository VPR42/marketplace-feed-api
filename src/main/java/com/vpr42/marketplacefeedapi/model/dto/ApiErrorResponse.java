package com.vpr42.marketplacefeedapi.model.dto;

import com.vpr42.marketplacefeedapi.model.enums.ApiError;

import java.util.List;
import java.util.Map;

public record ApiErrorResponse(
    Integer status,
    ApiError errorCode,
    String message,
    Map<String, List<String>> errors
) {
    public ApiErrorResponse(Integer status, ApiError errorCode, String message) {
        this(
            status,
            errorCode,
            message,
            Map.of()
        );
    }
}
