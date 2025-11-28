package com.vpr42.marketplacefeedapi.mappers;

import com.vpr42.marketplacefeedapi.model.dto.User;
import com.vpr42.marketplacefeedapi.model.entity.UserEntity;
import lombok.extern.slf4j.Slf4j;

/**
 * Маппер для информации о пользователе
 */
@Slf4j
public class UserMapper {
    public static User fromEntity(UserEntity user) {
        return new User(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getSurname(),
            user.getPatronymic(),
            user.getAvatarPath(),
            CityMapper.fromEntity(user.getCity()),
            user.getMasterInfo() != null
                    ? MasterInfoMapper.fromEntity(user.getMasterInfo())
                    : null
        );
    }
}
