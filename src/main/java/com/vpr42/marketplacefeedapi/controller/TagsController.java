package com.vpr42.marketplacefeedapi.controller;

import com.vpr42.marketplacefeedapi.model.dto.TagDto;
import com.vpr42.marketplacefeedapi.service.TagsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/feed/tags")
@RestController
@RequiredArgsConstructor
@Slf4j
public class TagsController {
    private final TagsService tagsService;

    @GetMapping
    public ResponseEntity<List<TagDto>> getAllTags() {
        log.info("Processing get all tags request");
        List<TagDto> tags = tagsService.getAllTags();

        return ResponseEntity.ok(tags);
    }
}