package com.vpr42.marketplacefeedapi.controller;

import com.vpr42.marketplacefeedapi.model.dto.AddToFavouriteDto;
import com.vpr42.marketplacefeedapi.model.entity.UserEntity;
import com.vpr42.marketplacefeedapi.service.FavouriteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/api/feed/favourites")
@RestController
@RequiredArgsConstructor
@Slf4j
public class FavouriteController {
    private final FavouriteService favouriteService;

    @PostMapping
    public ResponseEntity<Void> addToFavourite(@RequestBody @Valid AddToFavouriteDto dto,
                                              @AuthenticationPrincipal UserEntity user) {
        log.info("Adding job {} to favourites for user {}", dto.jobId(), user.getId());
        favouriteService.addToFavourite(dto, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<Void> removeFromFavourite(@PathVariable UUID jobId,
                                                    @AuthenticationPrincipal UserEntity user) {
        log.info("Removing job {} from favourites for user {}", jobId, user.getId());
        favouriteService.removeFromFavourite(jobId, user);
        return ResponseEntity.ok().build();
    }
}
