package com.vpr42.marketplacefeedapi.mappers;

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
            CityMapper.fromEntity(user.getCity()),
            MasterInfoMapper.fromEntity(user.getMasterInfo())
        );
    }
}
