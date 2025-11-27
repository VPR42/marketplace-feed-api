package com.vpr42.marketplacefeedapi.repository;

import com.vpr42.marketplacefeedapi.model.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Репозиторий для работы с городами
 */
public interface CityRepository extends JpaRepository<CityEntity, Integer>,
        JpaSpecificationExecutor<CityEntity> {

}
