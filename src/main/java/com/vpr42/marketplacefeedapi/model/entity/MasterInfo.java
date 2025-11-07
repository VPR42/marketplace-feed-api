package com.vpr42.marketplacefeedapi.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "masters_info")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(exclude = {"user", "skills"})
@EqualsAndHashCode(exclude = {"user", "skills"})
public class MasterInfo {
    @Id
    UUID id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    User user;

    @Column(name = "experience", nullable = false)
    Integer experience;

    @Column(name = "description", nullable = true)
    String description;

    @Column(name = "pseudonym", nullable = true, unique = true)
    String pseudonym;

    @Column(name = "phone_number", nullable = false, unique = true)
    String phoneNumber;

    @Column(name = "working_hours", nullable = true)
    String workingHours;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(
        name = "master_skills",
        joinColumns = @JoinColumn(name = "master_id"),
        inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    Set<Skill> skills;
}