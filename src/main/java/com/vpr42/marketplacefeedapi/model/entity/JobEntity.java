package com.vpr42.marketplacefeedapi.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Сущность услуги в БД
 */
@Entity
@Table(name = "jobs")
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"tags", "user", "category", "orders"})
@EqualsAndHashCode(exclude = {"tags", "user", "category", "orders"})
@NamedEntityGraph(
        name = "JobEntity_withAdditionalInfo",
        attributeNodes = {
                @NamedAttributeNode("category"),
                @NamedAttributeNode("tags"),
                @NamedAttributeNode(value = "user", subgraph = "subgraph.user")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "subgraph.user",
                        attributeNodes = {
                            @NamedAttributeNode(value = "masterInfo", subgraph = "subgraph.masterInfo"),
                            @NamedAttributeNode(value = "city")
                        }
                ),
                @NamedSubgraph(
                    name = "subgraph.masterInfo",
                    attributeNodes = {
                        @NamedAttributeNode("skills")
                    }
                )
        }
)
public class JobEntity {
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

    @Column(name = "cover_url")
    String coverUrl;

    @Column(name = "created_at", nullable = false)
    @Builder.Default
    LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_id")
    UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    CategoryEntity category;

    @OneToMany(mappedBy = "job", fetch = FetchType.LAZY)
    List<OrderEntity> orders;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "job_tags",
        joinColumns = @JoinColumn(name = "job_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Builder.Default
    Set<TagEntity> tags = new HashSet<>();
}
