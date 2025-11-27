package com.vpr42.marketplacefeedapi.config.authorization;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

/**
 * Настройка конфигурации безопасности
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

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
            .authorizeHttpRequests(req ->
                req
                    .requestMatchers("/api/feed/docs/**",
                            "/api/feed/swagger",
                            "/api/feed/swagger-ui/**").permitAll()
                    .requestMatchers("api/feed/**").authenticated()
            )
            .addFilterAfter(filter, LogoutFilter.class);

        return http.build();
    }

}
