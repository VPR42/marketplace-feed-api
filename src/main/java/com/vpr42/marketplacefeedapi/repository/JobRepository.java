package com.vpr42.marketplacefeedapi.repository;

import com.vpr42.marketplacefeedapi.model.entity.JobEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.UUID;

public interface JobRepository extends ListCrudRepository<JobEntity, UUID> {
}
