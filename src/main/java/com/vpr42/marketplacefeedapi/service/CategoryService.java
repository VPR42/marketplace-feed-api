package com.vpr42.marketplacefeedapi.service;

import com.vpr42.marketplacefeedapi.model.dto.CategoryWithCount;
import com.vpr42.marketplacefeedapi.model.enums.SortType;

import java.util.List;

public interface CategoryService {
    List<CategoryWithCount> getOrdered(String query, SortType sortByJobsCount);
}