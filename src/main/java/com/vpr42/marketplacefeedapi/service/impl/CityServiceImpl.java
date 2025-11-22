package com.vpr42.marketplacefeedapi.service.impl;

import com.vpr42.marketplacefeedapi.mappers.CityMapper;
import com.vpr42.marketplacefeedapi.mappers.JobsMapper;
import com.vpr42.marketplacefeedapi.model.dto.City;
import com.vpr42.marketplacefeedapi.model.entity.CityEntity;
import com.vpr42.marketplacefeedapi.repository.CityRepository;
import com.vpr42.marketplacefeedapi.repository.criteria.CityFiltersSpecification;
import com.vpr42.marketplacefeedapi.service.CityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;

    @Override
    @Transactional
    public Page<City> getCities(String query,
                                boolean withJobs,
                                boolean orderedByJobsCount,
                                int page,
                                int pageSize) {

        Specification<CityEntity> specification = CityFiltersSpecification.filtered(withJobs,
                orderedByJobsCount,
                query);
        var cities = cityRepository.findAll(specification, PageRequest.of(page, pageSize));
        log.info("Found: {}", cities.get().toList());
        return cities.map(CityMapper::fromEntity);
    }
}
