package com.vpr42.marketplacefeedapi.utils;

import com.vpr42.marketplacefeedapi.model.dto.User;
import com.vpr42.marketplacefeedapi.model.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserMapper {
    public static User fromEntity(UserEntity user) {
        log.info("Converting entity {} to dto", user);
        return new User(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getSurname(),
            user.getPatronymic(),
            user.getAvatarPath(),
            user.getCity()
        );
    }
}
