package com.vpr42.marketplacefeedapi.service;

import com.vpr42.marketplacefeedapi.model.dto.TagDto;
import java.util.List;

public interface TagsService {
    List<TagDto> getAllTags();
}