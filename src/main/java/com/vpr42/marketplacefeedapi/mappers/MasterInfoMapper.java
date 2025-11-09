package com.vpr42.marketplacefeedapi.mappers;

import com.vpr42.marketplacefeedapi.model.dto.Master;
import com.vpr42.marketplacefeedapi.model.entity.MasterInfoEntity;
import com.vpr42.marketplacefeedapi.model.entity.SkillEntity;

public class MasterInfoMapper {
    public static Master fromEntity(MasterInfoEntity entity) {
        return new Master(
            entity.getExperience(),
            entity.getDescription(),
            entity.getPseudonym(),
            entity.getPhoneNumber(),
            entity.getWorkingHours(),
            entity.getSkills()
                    .stream()
                    .map(SkillEntity::getName)
                    .toList()
        );
    }
}
