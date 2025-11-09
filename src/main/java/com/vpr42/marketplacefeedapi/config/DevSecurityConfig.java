package com.vpr42.marketplacefeedapi.config;

import com.vpr42.marketplacefeedapi.security.DevPreAuthenticationFilter;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

/**
 * Dev Only
 * Конфигурации Spring Security для разработки с дефолтным пользователем
 * */
@Configuration
@EnableWebSecurity
public class DevSecurityConfig {
    @Bean
    @SneakyThrows
    @Profile("dev")
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http
            .addFilterBefore(new DevPreAuthenticationFilter(), AbstractPreAuthenticatedProcessingFilter.class)
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(req -> req
                    .requestMatchers("/swagger-ui/**")
                    .permitAll()
                    .requestMatchers("/v3/api-docs*/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated());
        return http.build();
    }
}
