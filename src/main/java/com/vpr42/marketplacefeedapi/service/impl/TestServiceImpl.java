package com.vpr42.marketplacefeedapi.service.impl;

import com.vpr42.marketplacefeedapi.model.dto.User;
import com.vpr42.marketplacefeedapi.model.entity.CityEntity;
import com.vpr42.marketplacefeedapi.model.entity.UserEntity;
import com.vpr42.marketplacefeedapi.service.TestService;
import com.vpr42.marketplacefeedapi.utils.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    @Override
    @Transactional
    public User getTestUser() {
        log.info("Creating test data");

        var user = UserEntity.builder()
                .id(UUID.randomUUID())
                .name("TEST")
                .password("TEST")
                .email("test@mail.ru")
                .city(new CityEntity(1, "Ростовская область", "Ростов-на-Дону"))
                .build();

        log.info("Test data was created: {}", user);

        return UserMapper.fromEntity(user);
    }

}
