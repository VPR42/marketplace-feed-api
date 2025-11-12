package com.vpr42.marketplacefeedapi.repository;

import com.vpr42.marketplacefeedapi.model.entity.ServiceEntity;
import org.springframework.data.repository.ListPagingAndSortingRepository;

import java.util.UUID;

public interface ServiceRepository extends ListPagingAndSortingRepository<ServiceEntity, UUID> {
}
