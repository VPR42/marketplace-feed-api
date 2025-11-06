package com.vpr42.marketplacefeedapi.config;

import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

// Настройка конфигурации безопасности, сейчас просто разрешает любой запрос
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    @SneakyThrows
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http
            .authorizeHttpRequests(req -> {
                req.anyRequest().permitAll();
            })
            .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }

}
