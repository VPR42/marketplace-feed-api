package com.vpr42.marketplacefeedapi.repository;

import com.vpr42.marketplacefeedapi.model.entity.ServiceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ServiceRepository extends CrudRepository<ServiceEntity, UUID> {
}
