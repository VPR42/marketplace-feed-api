package com.vpr42.marketplacefeedapi.repository;

import com.vpr42.marketplacefeedapi.model.entity.TagEntity;
import org.springframework.data.repository.ListCrudRepository;

public interface TagsRepository extends ListCrudRepository<TagEntity, Integer> {
}
