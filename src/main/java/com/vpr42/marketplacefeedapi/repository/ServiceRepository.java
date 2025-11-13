package com.vpr42.marketplacefeedapi.repository;

import com.vpr42.marketplacefeedapi.model.entity.ServiceEntity;
import org.springframework.data.repository.ListCrudRepository;

import java.util.UUID;

public interface ServiceRepository extends ListCrudRepository<ServiceEntity, UUID> {
}
