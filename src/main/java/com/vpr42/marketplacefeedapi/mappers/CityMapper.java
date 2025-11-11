package com.vpr42.marketplacefeedapi.mappers;

import com.vpr42.marketplacefeedapi.model.dto.City;
import com.vpr42.marketplacefeedapi.model.entity.CityEntity;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CityMapper {
    public static City fromEntity(CityEntity city) {
        log.info("Converting CityEntity to dto: {}", city);

        return new City(
            city.getRegion(),
            city.getName()
        );
    }
}
