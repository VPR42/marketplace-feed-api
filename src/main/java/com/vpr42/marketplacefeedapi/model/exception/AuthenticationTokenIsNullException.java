package com.vpr42.marketplacefeedapi.model.exception;

import org.springframework.security.core.AuthenticationException;

public class AuthenticationTokenIsNullException extends AuthenticationException {
    public AuthenticationTokenIsNullException() {
        super("Invalid Authentication token");
    }
}
