package com.vpr42.marketplacefeedapi.service.impl;

import com.vpr42.marketplacefeedapi.model.entity.User;
import com.vpr42.marketplacefeedapi.service.TestService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TestServiceImpl implements TestService {

    @Override
    public User getTestUser() {
        return User.builder()
                .id(UUID.randomUUID())
                .name("TEST")
                .email("test@mail.ru")
                .build();
    }

}
