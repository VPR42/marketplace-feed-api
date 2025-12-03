package com.vpr42.marketplacefeedapi.config.authorization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vpr42.marketplacefeedapi.model.dto.ApiErrorResponse;
import com.vpr42.marketplacefeedapi.model.enums.ApiError;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Настройка конфигурации безопасности
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    @Profile("prod")
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService) throws Exception {
        FromHeaderAuthenticationProvider provider = new FromHeaderAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);

        return provider::authenticate;
    }

    @Bean
    @Profile("prod")
    public SecurityFilterChain filterChain(HttpSecurity http, ProdGatewayAuthenticationFilter filter)
            throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(req -> req
                    .anyRequest()
                    .permitAll()
            )
            .exceptionHandling(config -> {
                config.authenticationEntryPoint(
                        (request, response, authException) ->
                                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED)
                );
                config.accessDeniedHandler(
                        (request, response, accessDeniedException) -> {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    ApiErrorResponse errorResponse = new ApiErrorResponse(HttpServletResponse.SC_FORBIDDEN,
                            ApiError.FORBIDDEN,
                            ApiError.FORBIDDEN.name()
                    );
                    String json = objectMapper.writeValueAsString(errorResponse);

                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    response.getWriter().write(json);
                });
            })
            .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
