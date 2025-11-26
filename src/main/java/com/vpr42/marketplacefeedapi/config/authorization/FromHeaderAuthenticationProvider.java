package com.vpr42.marketplacefeedapi.config.authorization;

import com.vpr42.marketplacefeedapi.model.exception.AuthenticationTokenIsNullException;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Slf4j
@Setter
public class FromHeaderAuthenticationProvider implements AuthenticationProvider {
    private UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        FromHeaderAuthorizationToken token = (FromHeaderAuthorizationToken) authentication.getPrincipal();
        if (token == null) {
            log.error("User header token is null");
            throw new BadCredentialsException("Authentication token is null");
        }

        log.info("Trying to authenticate user with id: {}", token.getId());
        try {
            UserDetails user = userDetailsService.loadUserByUsername(token.getEmail());
            var authToken = new FromHeaderAuthorizationToken(token.getId(),
                    token.getEmail(),
                    user);
            SecurityContextHolder.getContext().setAuthentication(authToken);

            return authToken;
        } catch (UsernameNotFoundException ex) {
            log.error("Invalid credentials during authentication for user with id: {}", token.getId());

            throw ex;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(FromHeaderAuthenticationProvider.class);
    }
}
