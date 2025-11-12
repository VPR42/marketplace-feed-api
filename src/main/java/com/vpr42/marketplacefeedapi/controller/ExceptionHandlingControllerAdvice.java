package com.vpr42.marketplacefeedapi.controller;

import com.vpr42.marketplacefeedapi.model.dto.ApiErrorResponse;
import com.vpr42.marketplacefeedapi.model.enums.ApiError;
import com.vpr42.marketplacefeedapi.model.exception.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class ExceptionHandlingControllerAdvice {

    /**
     * Ошибки валидации
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationErrors(MethodArgumentNotValidException exception) {
        var errors = exception.getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())
                ));

        return ResponseEntity
                .badRequest()
                .body(new ApiErrorResponse(
                    ApiError.INVALID_DATA,
                    exception.getMessage(),
                    errors
                ));
    }

    /**
     * Внутренние ошибки приложения
     */
    @ExceptionHandler
    public ResponseEntity<ApiErrorResponse> handleApplicationException(ApplicationException exception) {
        log.error("An error occurred processing request", exception);

        return ResponseEntity
                .status(exception.getStatusCode())
                .body(new ApiErrorResponse(
                    exception.getError(),
                    exception.getMessage()
                ));
    }

    /**
     * Сюда будем добавлять разные виды исключений и их обработку
     * Handler пишется по такому принципу
     */
    @ExceptionHandler
    public ResponseEntity<?> handleException(Exception ex) {
        log.error("An error occurred processing request", ex);

        return ResponseEntity
                .internalServerError()
                .build();
    }
}
