package com.vpr42.marketplacefeedapi.repository;

import com.vpr42.marketplacefeedapi.model.entity.JobEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface JobRepository extends ListCrudRepository<JobEntity, UUID> {
    @Query("""
            SELECT job
            FROM JobEntity job
            INNER JOIN job.masterInfo info
            WHERE info.id = :masterId
            AND job.name = :name
        """)
    Optional<JobEntity> findByMasterIdAndName(UUID masterId, String name);
}
