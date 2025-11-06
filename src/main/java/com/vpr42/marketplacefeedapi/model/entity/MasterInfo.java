package com.vpr42.marketplacefeedapi.model.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MasterInfo {
    User user;
    Integer experience;
    String description;
    String pseudonym;
    String phoneNumber;
    String workingHours;
    List<Skill> skills;
}