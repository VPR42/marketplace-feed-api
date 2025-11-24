package com.vpr42.marketplacefeedapi.service;

import com.vpr42.marketplacefeedapi.model.dto.Category;
import com.vpr42.marketplacefeedapi.model.enums.SortType;

import java.util.List;

public interface CategoryService {
    List<Category> getOrdered(SortType sortByJobsCount);
}