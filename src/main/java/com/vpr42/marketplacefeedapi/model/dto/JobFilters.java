package com.vpr42.marketplacefeedapi.model.dto;

import com.vpr42.marketplacefeedapi.model.enums.SortType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class JobFilters {
    @Min(value = 0, message = "Page number can't be less then 0")
    private Integer page = 0;
    @Min(value = 1, message = "Page must contain elements")
    private Integer pageSize = 15;
    private UUID masterId;
    @Size(min = 1, message = "Query can't be empty")
    private String query;
    @Min(value = 1, message = "Category id must be greater than 0")
    private Integer categoryId;
    @Size(min = 1, message = "Skills must contain at least one element")
    private String[] skills;
    @Size(min = 1, message = "Tags must contain at least one element")
    private String[] tags;
    @Min(value = 0, message = "MinPrice must be positive or zero value")
    private Integer minPrice;
    @Min(value = 0, message = "MaxPrice must be positive or zero value")
    private Integer maxPrice;
    @Min(value = 0, message = "Experience must be positive or zero value")
    private Integer experience;
    @Min(value = 1, message = "City id must be greater than 1")
    private Integer cityId;
    @Min(value = 0, message = "MinOrders must be positive or zero value")
    private Integer minOrders;
    private SortType priceSort;
    private SortType ordersCountSort;
    private SortType experienceSort;
}
