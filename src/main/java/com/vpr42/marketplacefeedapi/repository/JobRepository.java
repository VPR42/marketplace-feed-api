package com.vpr42.marketplacefeedapi.repository;

import com.vpr42.marketplacefeedapi.model.dto.JobEntityWithCount;
import com.vpr42.marketplacefeedapi.model.entity.JobEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JobRepository extends JpaRepository<JobEntity, UUID>, JpaSpecificationExecutor<JobEntity> {
    @Query("""
            SELECT job
            FROM JobEntity job
            INNER JOIN job.masterInfo info
            WHERE info.id = :masterId
            AND job.name = :name
        """)
    Optional<JobEntity> findByMasterIdAndName(UUID masterId, String name);

    @Query("""
            SELECT new com.vpr42.marketplacefeedapi.model.dto.JobEntityWithCount(j, COUNT(o))
            FROM JobEntity j
            LEFT JOIN j.orders o
            WHERE j.id IN :list
            GROUP BY j.id
        """)
    List<JobEntityWithCount> findOrdersCountFor(@Param("list") List<UUID> list);

    @Query(
        """
        FROM JobEntity j
        WHERE j.id IN :list
        """
    )
    @EntityGraph("JobEntity_withAdditionalInfo")
    List<JobEntity> findAllEntitiesWithIds(@Param("list") List<UUID> list);

    boolean existsByIdAndMasterInfo_User_Id(UUID jobId, UUID userId);
}
