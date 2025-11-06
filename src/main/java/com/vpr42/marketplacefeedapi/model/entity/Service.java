package com.vpr42.marketplacefeedapi.model.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

// Вот это все непотребство скомпилится в POJO с геттерами и сеттерами, приватными полями, toString, equals и hashCode
// и конструкторами лего дупло
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class Service {
    UUID id;
    String name;
    String description;
    BigDecimal price;
    String coverUrl;
    LocalDateTime createdAt;
    MasterInfo masterInfo;
    Category category;
    List<Tag> tags;
}
