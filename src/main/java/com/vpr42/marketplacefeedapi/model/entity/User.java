package com.vpr42.marketplacefeedapi.model.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    UUID id;
    String email;
    String password;
    String name;
    String surname;
    String patronymic;
    String avatarPath;
    Instant createdAt;
    City city;
}
