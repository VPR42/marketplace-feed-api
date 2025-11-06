package com.vpr42.marketplacefeedapi.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionHandlingControllerAdvice {
    // Сюда будем добавлять разные виды исключений и их обработку
    // Handler пишется по такому принципу
    @ExceptionHandler
    public ResponseEntity<?> handleException(Exception ex) {
        log.error("An error occurred processing request", ex);

        return ResponseEntity
                .internalServerError()
                .build();
    }
}
