package com.vpr42.marketplacefeedapi.service.impl;

import com.vpr42.marketplacefeedapi.mappers.CategoryMapper;
import com.vpr42.marketplacefeedapi.model.dto.CategoryWithCount;
import com.vpr42.marketplacefeedapi.model.enums.SortType;
import com.vpr42.marketplacefeedapi.repository.CategoryRepository;
import com.vpr42.marketplacefeedapi.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public List<CategoryWithCount> getOrdered(String query, SortType sortByJobsCount) {
        List<CategoryWithCount> categories = categoryRepository.findAllWithCount(query, sortByJobsCount)
                .stream()
                .map(category -> new CategoryWithCount(
                    CategoryMapper.fromEntity(category.category()),
                    category.count()
                ))
                .toList();

        log.info("Fetched categories: {}", categories);
        return categories;
    }
}
