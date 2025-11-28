package com.vpr42.marketplacefeedapi.service;

import com.vpr42.marketplacefeedapi.model.dto.City;

import java.util.List;

public interface CityService {
    List<City> getCities(String query,
                         boolean withJobs,
                         boolean orderedByJobsCount);
}
