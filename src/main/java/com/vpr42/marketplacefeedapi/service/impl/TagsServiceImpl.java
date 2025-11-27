package com.vpr42.marketplacefeedapi.service.impl;

import com.vpr42.marketplacefeedapi.mappers.TagMapper;
import com.vpr42.marketplacefeedapi.model.dto.TagDto;
import com.vpr42.marketplacefeedapi.model.entity.TagEntity;
import com.vpr42.marketplacefeedapi.repository.TagsRepository;
import com.vpr42.marketplacefeedapi.service.TagsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagsServiceImpl implements TagsService {
    private final TagsRepository tagsRepository;

    @Override
    public List<TagDto> getAllTags() {
        List<TagEntity> tags = tagsRepository.findAll();
        log.info("Fetched {} tags from database", tags.size());

        return TagMapper.fromEntities(tags);
    }
}