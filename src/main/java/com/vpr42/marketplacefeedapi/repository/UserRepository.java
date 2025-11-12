package com.vpr42.marketplacefeedapi.repository;

import com.vpr42.marketplacefeedapi.model.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends ListPagingAndSortingRepository<UserEntity, UUID> {
    @EntityGraph(value = "UserEntity_withAdditionalInfo")
    Optional<UserEntity> findByEmail(String email);
    boolean existsByEmail(String email);
}
