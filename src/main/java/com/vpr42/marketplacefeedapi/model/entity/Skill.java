package com.vpr42.marketplacefeedapi.model.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class Skill {
    Integer id;
    String name;
}
