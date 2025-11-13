package com.vpr42.marketplacefeedapi.controller;

import com.vpr42.marketplacefeedapi.model.dto.User;
import com.vpr42.marketplacefeedapi.model.entity.UserEntity;
import com.vpr42.marketplacefeedapi.service.TestService;
import com.vpr42.marketplacefeedapi.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Только пример как создавать контроллер
 * Работает только при разработке для проверки
 */
@Profile("dev")
@RestController
@RequestMapping("/api/test")
@Slf4j
@RequiredArgsConstructor
public class TestController {
    private final TestService testService;
    /**
     * Доступно по /api/test
     */
    @GetMapping
    public User getTestData(@AuthenticationPrincipal UserEntity user) {
        log.info("Processing new test request");

        return UserMapper.fromEntity(user);
    }

}
