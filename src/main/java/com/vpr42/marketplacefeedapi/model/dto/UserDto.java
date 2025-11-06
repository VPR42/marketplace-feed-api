package com.vpr42.marketplacefeedapi.model.dto;

import com.vpr42.marketplacefeedapi.model.entity.City;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    UUID id;
    String email;
    String name;
    String surname;
    String patronymic;
    String avatarPath;
    City city;
}
