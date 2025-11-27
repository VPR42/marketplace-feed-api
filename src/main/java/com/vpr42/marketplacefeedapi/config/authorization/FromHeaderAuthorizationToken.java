package com.vpr42.marketplacefeedapi.config.authorization;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.UUID;

@Getter
public class FromHeaderAuthorizationToken extends AbstractAuthenticationToken {
    private final UUID id;
    private final String email;
    private final UserDetails details;

    public FromHeaderAuthorizationToken(UUID id, String email) {
        super(null);
        this.id = id;
        this.email = email;
        this.details = null;
        setAuthenticated(false);
    }

    public FromHeaderAuthorizationToken(UUID id, String email, UserDetails details) {
        super(details.getAuthorities());
        this.id = id;
        this.email = email;
        this.details = details;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public UserDetails getPrincipal() {
        return details;
    }
}
