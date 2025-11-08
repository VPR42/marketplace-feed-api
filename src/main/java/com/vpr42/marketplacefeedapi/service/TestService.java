package com.vpr42.marketplacefeedapi.service;

import com.vpr42.marketplacefeedapi.model.dto.User;
import org.springframework.context.annotation.Profile;

@Profile("dev")
public interface TestService {
    User getTestUser();
}
