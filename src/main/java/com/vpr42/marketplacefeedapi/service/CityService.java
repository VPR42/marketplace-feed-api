package com.vpr42.marketplacefeedapi.service;

import com.vpr42.marketplacefeedapi.model.dto.City;
import org.springframework.data.domain.Page;

public interface CityService {
    Page<City> getCities();
}
