package com.vpr42.marketplacefeedapi.service.impl;

import com.vpr42.marketplacefeedapi.model.dto.UserDto;
import com.vpr42.marketplacefeedapi.model.entity.City;
import com.vpr42.marketplacefeedapi.model.entity.User;
import com.vpr42.marketplacefeedapi.service.TestService;
import com.vpr42.marketplacefeedapi.utils.fabric.UserFabric;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    UserFabric fabric;

    @Override
    @Transactional
    public UserDto getTestUser() {
        var user = User.builder()
                .id(UUID.randomUUID())
                .name("TEST")
                .password("TEST")
                .email("test@mail.ru")
                .build();

        return fabric.fromEntity(user);
    }

}
