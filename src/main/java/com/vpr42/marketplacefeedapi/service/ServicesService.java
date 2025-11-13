package com.vpr42.marketplacefeedapi.service;

import com.vpr42.marketplacefeedapi.model.dto.CreateServiceDto;
import com.vpr42.marketplacefeedapi.model.dto.Service;
import com.vpr42.marketplacefeedapi.model.entity.UserEntity;

public interface ServicesService {
    Service createService(CreateServiceDto dto, UserEntity initiator);
}
