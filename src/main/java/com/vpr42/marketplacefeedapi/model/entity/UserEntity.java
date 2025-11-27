package com.vpr42.marketplacefeedapi.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Сущность пользователя в БД
 */
@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(exclude = {"masterInfo", "favouriteServices", "orders", "password"})
@EqualsAndHashCode(exclude = {"city", "masterInfo", "favouriteServices", "orders"})
@NamedEntityGraph(
    name = "UserEntity_withAdditionalInfo",
    attributeNodes = {
            @NamedAttributeNode("city"),
            @NamedAttributeNode(value = "masterInfo", subgraph = "subgraph.masterInfo")
    },
    subgraphs = {
        @NamedSubgraph(
            name = "subgraph.masterInfo",
            attributeNodes = { @NamedAttributeNode("skills") }
        )
    }
)
public class UserEntity implements UserDetails {

    @Id
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
    CityEntity city;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    MasterInfoEntity masterInfo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @Builder.Default
    List<FavouriteJobEntity> favouriteServices = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @Builder.Default
    List<OrderEntity> orders = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
