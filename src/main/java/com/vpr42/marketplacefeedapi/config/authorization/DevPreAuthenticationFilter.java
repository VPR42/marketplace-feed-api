package com.vpr42.marketplacefeedapi.config.authorization;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Dev Only, задает стандартного пользователя для любого request-а
 */
@Component
@RequiredArgsConstructor
@Profile("dev")
public class DevPreAuthenticationFilter extends OncePerRequestFilter {
    private static final String TEST_USER_EMAIL = "test@mail.ru";

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException  {

        UserDetails user = userDetailsService.loadUserByUsername(TEST_USER_EMAIL);
        var auth = new PreAuthenticatedAuthenticationToken(
                user,
                null,
                user.getAuthorities()
        );
        logger.info("Add test data to request context");

        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);
    }

}
