package com.vpr42.marketplacefeedapi.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

@ConfigurationProperties(prefix = "api.cover")
public record CoverProperties(
    Set<String> allowedExtensions
) {
}
