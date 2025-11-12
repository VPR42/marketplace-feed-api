package com.vpr42.marketplacefeedapi.repository;

import com.vpr42.marketplacefeedapi.model.entity.CityEntity;
import org.springframework.data.repository.ListPagingAndSortingRepository;

/**
 * Репозиторий для работы с городами
 */
public interface CityRepository extends ListPagingAndSortingRepository<CityEntity, Integer> {
}
