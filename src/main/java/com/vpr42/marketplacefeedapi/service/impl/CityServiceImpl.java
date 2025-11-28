package com.vpr42.marketplacefeedapi.service.impl;

import com.vpr42.marketplacefeedapi.mappers.CityMapper;
import com.vpr42.marketplacefeedapi.model.dto.City;
import com.vpr42.marketplacefeedapi.model.entity.CityEntity;
import com.vpr42.marketplacefeedapi.repository.CityRepository;
import com.vpr42.marketplacefeedapi.repository.criteria.CityFiltersSpecification;
import com.vpr42.marketplacefeedapi.service.CityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;

    @Override
    @Transactional
    public List<City> getCities(String query,
                                boolean withJobs,
                                boolean orderedByJobsCount) {

        Specification<CityEntity> specification = CityFiltersSpecification.filtered(withJobs,
                orderedByJobsCount,
                query);
        var cities = cityRepository.findAll(specification);
        log.info("Found cities: {}", cities);
        return cities.stream()
                .map(CityMapper::fromEntity)
                .toList();
    }
}
