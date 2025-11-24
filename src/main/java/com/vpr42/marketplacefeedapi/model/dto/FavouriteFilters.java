package com.vpr42.marketplacefeedapi.model.dto;

import com.vpr42.marketplacefeedapi.model.enums.SortType;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.util.UUID;

@Data
public class FavouriteFilters {

    @Min(value = 0, message = "Page number can't be less then 0")
    private Integer page = 0;

    @Min(value = 1, message = "Page must contain elements")
    private Integer pageSize = 15;

    private String query;

    @Min(value = 1, message = "Category id must be greater than 0")
    private Integer categoryId;

    private String[] tags;

    @Min(value = 0, message = "MinPrice must be positive or zero value")
    private Integer minPrice;

    @Min(value = 0, message = "MaxPrice must be positive or zero value")
    private Integer maxPrice;

    private SortType priceSort;

    private SortType ordersCountSort;

    private SortType createdAtSort;
}