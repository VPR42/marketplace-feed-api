package com.vpr42.marketplacefeedapi.controller;

import com.vpr42.marketplacefeedapi.model.dto.AddToFavouriteDto;
import com.vpr42.marketplacefeedapi.model.dto.ApiErrorResponse;
import com.vpr42.marketplacefeedapi.model.entity.UserEntity;
import com.vpr42.marketplacefeedapi.service.FavouriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Favourites", description = "Контроллер для избранных")
public class FavouriteController {
    private final FavouriteService favouriteService;

    @PostMapping
    @Operation(
        summary = "Добавить в избранное",
        responses = {
            @ApiResponse(description = "Услуга добавлена", responseCode = "200"),
            @ApiResponse(description = "Добавление своей услуги", responseCode = "400",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = {
                        @ExampleObject(
                            name = "SelfAdd",
                            summary = "Добавление своей услуги",
                            value = """
                            {
                                "status": 400,
                                "errorCode": "SELF_FAVOURITE",
                                "message": "Attempted to add your own job to favourites",
                                "errors": null
                            }
                            """
                        )
                    }
                    )),
            @ApiResponse(description = "Услуга не найдена", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class),
                examples = {
                    @ExampleObject(
                        name = "NotFound",
                        summary = "Услуга не найдена",
                        value = """
                        {
                            "status": 404,
                            "errorCode": "JOB_NOT_FOUND",
                            "message": "Job with id 000-00 is not found",
                            "errors": null
                        }
                        """
                    )
                }
            )),
        }
    )
    public ResponseEntity<Void> addToFavourite(@RequestBody @Valid AddToFavouriteDto dto,
                                              @AuthenticationPrincipal UserEntity user) {
        log.info("Adding job {} to favourites for user {}", dto.jobId(), user.getId());
        favouriteService.addToFavourite(dto, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{jobId}")
    @Operation(
        summary = "Удалить из избранного",
        responses = {
            @ApiResponse(description = "Услуга удалена", responseCode = "200")
        }
    )
    public ResponseEntity<Void> removeFromFavourite(@PathVariable UUID jobId,
                                                    @AuthenticationPrincipal UserEntity user) {
        log.info("Removing job {} from favourites for user {}", jobId, user.getId());
        favouriteService.removeFromFavourite(jobId, user);
        return ResponseEntity.ok().build();
    }
}
