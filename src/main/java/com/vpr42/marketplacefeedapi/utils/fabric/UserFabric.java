package com.vpr42.marketplacefeedapi.utils.fabric;

import com.vpr42.marketplacefeedapi.model.dto.UserDto;
import com.vpr42.marketplacefeedapi.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserFabric { // ну тут по-хорошему тоже интерфейс, но лан че уже
    public UserDto fromEntity(User user) {
        return UserDto
                .builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .city(user.getCity())
                .avatarPath(user.getAvatarPath())
                .patronymic(user.getPatronymic())
                .surname(user.getSurname())
                .build();
    }
}
