package com.vpr42.marketplacefeedapi.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MasterDto {
    UserDto user;
    Integer experience;
    String description;
    String pseudonym;
    String phoneNumber;
    String workingHours;
    List<String> skills;
}
