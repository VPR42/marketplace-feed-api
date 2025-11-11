package com.vpr42.marketplacefeedapi.service.impl;

import com.vpr42.marketplacefeedapi.repository.UserRepository;
import com.vpr42.marketplacefeedapi.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Сервис для взаимодействия с пользователями, используется для аутентификации
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
