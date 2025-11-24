package com.vpr42.marketplacefeedapi.repository;

import com.vpr42.marketplacefeedapi.model.entity.OrderEntity;
import com.vpr42.marketplacefeedapi.model.enums.OrderStatus;
import org.springframework.data.repository.ListCrudRepository;

import java.util.UUID;

public interface OrderRepository extends ListCrudRepository<OrderEntity, Long> {

    int countByJobId(UUID jobId);
}
