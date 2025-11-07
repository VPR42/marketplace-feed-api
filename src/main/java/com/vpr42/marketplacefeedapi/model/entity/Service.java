package com.vpr42.marketplacefeedapi.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

// Вот это все непотребство скомпилится в POJO с геттерами и сеттерами, приватными полями, toString, equals и hashCode
// и конструкторами лего дупло
@Entity
@Table(name = "services")
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"tags", "masterInfo", "category"})
@EqualsAndHashCode(exclude = {"tags", "masterInfo", "category"})
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    UUID id;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "description", nullable = false)
    String description;

    @Column(name = "price", nullable = false)
    BigDecimal price;

    @Column(name = "cover_url", nullable = true)
    String coverUrl;

    @Column(name = "created_at", nullable = false)
    @Builder.Default
    LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_id")
    MasterInfo masterInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    Category category;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "services_tags",
        joinColumns = @JoinColumn(name = "service_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    Set<Tag> tags;
}
