package com.vpr42.marketplacefeedapi.service;

import com.vpr42.marketplacefeedapi.model.entity.CityEntity;
import com.vpr42.marketplacefeedapi.model.entity.MasterInfoEntity;
import com.vpr42.marketplacefeedapi.model.entity.UserEntity;
import com.vpr42.marketplacefeedapi.repository.CityRepository;
import com.vpr42.marketplacefeedapi.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

/**
 * Инициализирует начальные данные для Dev-режима
 */
@Service
@Profile("dev")
@RequiredArgsConstructor
public class DevDataInitializerService {
    private final CityRepository cityRepository;
    private final UserRepository userRepository;

    @PostConstruct
    @Transactional
    public void initialize() {
        if (cityRepository.count() > 0)
            return;

        CityEntity city = CityEntity
                .builder()
                .region("Ростовская область")
                .name("Ростов-на-Дону")
                .build();
        CityEntity savedCity = cityRepository.save(city);
        UserEntity user = UserEntity.builder()
                .id(UUID.fromString("58da1841-6609-4d27-949e-73218ce48922"))
                .email("test@mail.ru")
                .name("Иван")
                .surname("Иванов")
                .patronymic("Иванович")
                .password("{noop}123456")
                .createdAt(Instant.now())
                .city(savedCity)
                .avatarPath("https://somephoto.com")
                .build();

        MasterInfoEntity masterInfo = MasterInfoEntity.builder()
                .user(user)
                .description("Занимаюсь разной работой")
                .phoneNumber("0000000000")
                .experience(3)
                .pseudonym("Иван-Работник")
                .workingHours("9:00-19:00")
                .build();

        user.setMasterInfo(masterInfo);
        userRepository.save(user);
    }
}
