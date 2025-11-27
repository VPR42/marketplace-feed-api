package com.vpr42.marketplacefeedapi.mappers;

import com.vpr42.marketplacefeedapi.model.dto.Master;
import com.vpr42.marketplacefeedapi.model.entity.MasterInfoEntity;
import com.vpr42.marketplacefeedapi.model.entity.SkillEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;

/**
 * Маппер для информации о мастере
 */
@Slf4j
public class MasterInfoMapper {
    public static Master fromEntity(MasterInfoEntity entity) {
        log.info("Converting MasterInfoEntity to dto: {}", entity);

        return new Master(
            entity.getExperience(),
            entity.getDescription(),
            entity.getPseudonym(),
            entity.getPhoneNumber(),
            entity.getWorkingHours(),
            entity.getSkills()
                    .stream()
                    .map(SkillEntity::getName)
                    .collect(Collectors.toSet())
        );
    }
}
