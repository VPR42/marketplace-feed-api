package com.vpr42.marketplacefeedapi.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MapsId;
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

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Сущность доп. инфы о мастере в БД
 */
@Entity
@Table(name = "masters_info")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(exclude = {"user", "skills"})
@EqualsAndHashCode(exclude = {"user", "skills"})
public class MasterInfoEntity {
    @Id
    UUID id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    UserEntity user;

    @Column(name = "experience", nullable = false)
    Integer experience;

    @Column(name = "description")
    String description;

    @Column(name = "pseudonym", unique = true)
    String pseudonym;

    @Column(name = "phone_number", nullable = false, unique = true)
    String phoneNumber;

    @Column(name = "working_hours")
    String workingHours;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
        name = "master_skills",
        joinColumns = @JoinColumn(name = "master_id"),
        inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    @Builder.Default
    Set<SkillEntity> skills = new HashSet<>();
}