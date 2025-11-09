package com.vpr42.marketplacefeedapi.security;

import java.time.Instant;
import java.util.UUID;

/**
 * Используется для получения информации о пользователе
 * в запросе
 * */
public record UserDetailsEntity(
    UUID userId,
    String email,
    Instant expirationDate
) {
}
