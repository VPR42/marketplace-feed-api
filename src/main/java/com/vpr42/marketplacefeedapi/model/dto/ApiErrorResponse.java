package com.vpr42.marketplacefeedapi.model.dto;

import com.vpr42.marketplacefeedapi.model.enums.ApiError;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class ApiErrorResponse {
    private final ApiError errorCode;
    private final String message;
    private final Map<String, List<String>> errors;

    public ApiErrorResponse(ApiError errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
        this.errors = Map.of();
    }

    public ApiErrorResponse(ApiError errorCode, String message, Map<String, List<String>> errors) {
        this.errorCode = errorCode;
        this.message = message;
        this.errors = errors;
    }
}
