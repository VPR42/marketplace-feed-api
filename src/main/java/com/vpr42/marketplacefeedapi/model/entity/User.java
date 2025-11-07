package com.vpr42.marketplacefeedapi.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(exclude = {"city", "masterInfo", "favouriteServices", "orders"})
@EqualsAndHashCode(exclude = {"city", "masterInfo", "favouriteServices", "orders"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    UUID id;

    @Column(name = "email", unique = true, nullable = false)
    String email;

    @Column(name = "password", nullable = false)
    String password;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "surname", nullable = false)
    String surname;

    @Column(name = "patronymic", nullable = false)
    String patronymic;

    @Column(name = "avatar_path", nullable = false)
    String avatarPath;

    @Builder.Default
    @Column(name = "created_at", nullable = false)
    Instant createdAt = Instant.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city")
    City city;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    MasterInfo masterInfo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    List<FavouriteService> favouriteServices;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    List<Order> orders;
}
