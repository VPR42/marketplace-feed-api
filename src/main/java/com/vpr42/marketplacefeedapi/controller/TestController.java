package com.vpr42.marketplacefeedapi.controller;

import com.vpr42.marketplacefeedapi.model.dto.UserDto;
import com.vpr42.marketplacefeedapi.service.TestService;
import com.vpr42.marketplacefeedapi.utils.fabric.UserFabric;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Только пример как создавать контроллер

@Profile("dev") // Работает только при разработке, чтобы было удобнее работать и тд
@RestController
@RequestMapping("/api/test")
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TestController {

    TestService testService;
    UserFabric userFabric;

    // Доступно по /api/test
    @GetMapping
    public ResponseEntity<UserDto> getTestData() {
        log.info("Processing new request");

        return ResponseEntity
            .ok(
                userFabric.fromEntity(testService.getTestUser())
            );
    }

}
