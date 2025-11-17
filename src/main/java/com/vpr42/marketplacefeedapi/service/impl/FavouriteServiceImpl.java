package com.vpr42.marketplacefeedapi.service.impl;

import com.vpr42.marketplacefeedapi.model.dto.AddToFavouriteDto;
import com.vpr42.marketplacefeedapi.model.entity.FavouriteJobEntity;
import com.vpr42.marketplacefeedapi.model.entity.JobEntity;
import com.vpr42.marketplacefeedapi.model.entity.UserEntity;
import com.vpr42.marketplacefeedapi.model.entity.keys.FavouriteKey;
import com.vpr42.marketplacefeedapi.model.exception.JobNotFoundException;
import com.vpr42.marketplacefeedapi.repository.FavouriteJobRepository;
import com.vpr42.marketplacefeedapi.repository.JobRepository;
import com.vpr42.marketplacefeedapi.service.FavouriteService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavouriteServiceImpl implements FavouriteService {
    private final FavouriteJobRepository favouriteJobRepository;
    private final JobRepository jobRepository;

    @Override
    @Transactional
    public void addToFavourite(AddToFavouriteDto dto, UserEntity user) {
        if (isJobInFavourite(dto.jobId(), user)) {
            log.info("Job {} is already in favourites for user {}", dto.jobId(), user.getId());

            return;
        }

        JobEntity job = jobRepository.findById(dto.jobId())
                .orElseThrow(() -> new JobNotFoundException(dto.jobId()));

        FavouriteKey key = new FavouriteKey(user.getId(), dto.jobId());
        FavouriteJobEntity favourite = FavouriteJobEntity.builder()
                .key(key)
                .user(user)
                .service(job)
                .build();

        favouriteJobRepository.save(favourite);
        log.info("Job {} added to favourites for user {}", dto.jobId(), user.getId());
    }

    @Override
    @Transactional
    public boolean isJobInFavourite(UUID jobId, UserEntity user) {
        FavouriteKey key = new FavouriteKey(user.getId(), jobId);

        return favouriteJobRepository.existsById(key);
    }
}
