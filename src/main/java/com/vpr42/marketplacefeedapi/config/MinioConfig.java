package com.vpr42.marketplacefeedapi.config;

import com.vpr42.marketplacefeedapi.config.properties.MinioProperties;
import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MinioConfig {
    private final MinioProperties minioProperties;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioProperties.urls().apiUrl())
                .credentials(minioProperties.login(), minioProperties.password())
                .build();
    }

}
