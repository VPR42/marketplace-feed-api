package com.vpr42.marketplacefeedapi.model.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class City {
    Integer id;
    String region;
    String name;
}
