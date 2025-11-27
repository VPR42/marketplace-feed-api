package com.vpr42.marketplacefeedapi.repository;

import com.vpr42.marketplacefeedapi.model.entity.TagEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.List;
import java.util.Set;

public interface TagsRepository extends ListCrudRepository<TagEntity, Integer> {
    Set<TagEntity> findByIdIn(Set<Integer> ids);
}
