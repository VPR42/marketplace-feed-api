package com.vpr42.marketplacefeedapi.service;

import com.vpr42.marketplacefeedapi.model.dto.AddToFavouriteDto;
import com.vpr42.marketplacefeedapi.model.dto.FavouriteFilters;
import com.vpr42.marketplacefeedapi.model.dto.Job;
import com.vpr42.marketplacefeedapi.model.entity.UserEntity;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface FavouriteService {
    void addToFavourite(AddToFavouriteDto dto, UserEntity user);
    void removeFromFavourite(UUID jobId, UserEntity user);
    boolean isJobInFavourite(UUID jobId, UserEntity user);
    Page<Job> getFavouriteJobs(FavouriteFilters filters, UserEntity user);
}
