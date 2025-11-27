package com.vpr42.marketplacefeedapi.config.authorization;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProdGatewayAuthenticationFilter extends OncePerRequestFilter {
    private final AuthenticationManager authenticationManager;;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        log.info("Processing new user request");

        String idValue = request.getHeader("id");
        String emailValue = request.getHeader("email");
        log.info("Extracted: id: {}, email: {}", idValue, emailValue);
        if (idValue == null || emailValue == null
                || idValue.isBlank() || emailValue.isBlank()) {
            log.info("User doesn't have credentials info from gateway");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            UUID id = UUID.fromString(idValue);
            Authentication authentication = new FromHeaderAuthorizationToken(id, emailValue);
            this.authenticationManager.authenticate(authentication);
        } catch (IllegalArgumentException exception) {
            log.error("Invalid credentials. Id can't be null");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            return;
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            log.error("Failed to authenticate user. Reason", ex);

            return;
        }

        filterChain.doFilter(request, response);
    }
}
