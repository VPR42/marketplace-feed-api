package com.vpr42.marketplacefeedapi.model.dto;

import com.vpr42.marketplacefeedapi.model.enums.ApiError;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

/**
 * Ответ об ошибке для клиента
 * @param status Статус код ответа (HTTP)
 * @param errorCode Код ошибки приложения
 * @param message Сообщение об ошибке
 * @param errors Ошибки валидации
 */
@Schema(description = "Информация об ошибке")
public record ApiErrorResponse(
    @Schema(description = "Статус код ошибки", example = "404")
    Integer status,
    @Schema(description = "Код ошибки", example = "NOT_FOUND")
    ApiError errorCode,
    @Schema(description = "Сообщение об ошибке", example = "Data is not found")
    String message,
    @Schema(description = "Список ошибок при валидации в виде ключ - список ошибок")
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
