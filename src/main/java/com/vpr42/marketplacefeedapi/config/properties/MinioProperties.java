package com.vpr42.marketplacefeedapi.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.minio")
public record MinioProperties(
    String login,
    String password,
    String bucket,
    MinioUrl urls
) {
    public record MinioUrl(
        String apiUrl,
        String publicUrl
    ) {}
}
