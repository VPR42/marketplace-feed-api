package com.vpr42.marketplacefeedapi.controller;

import com.vpr42.marketplacefeedapi.model.dto.ApiErrorResponse;
import com.vpr42.marketplacefeedapi.model.dto.CoverUploadResponse;
import com.vpr42.marketplacefeedapi.model.dto.CreateJobDto;
import com.vpr42.marketplacefeedapi.model.dto.Job;
import com.vpr42.marketplacefeedapi.model.dto.JobFilters;
import com.vpr42.marketplacefeedapi.model.dto.UpdateJobDto;
import com.vpr42.marketplacefeedapi.model.entity.UserEntity;
import com.vpr42.marketplacefeedapi.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RequestMapping("/api/feed/jobs")
@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Jobs", description = "Контроллер для работы с услугами")
public class JobController {
    private final JobService jobService;

    @PostMapping
    @Operation(summary = "Создание услуги", responses = {
            @ApiResponse(responseCode = "200", description = "Услуга создана",
                content = @Content(schema = @Schema(implementation = Job.class))),
            @ApiResponse(responseCode = "400", description = "Ошибки валидации",
                content = @Content(schema = @Schema(implementation = ApiErrorResponse.class),
                examples = {
                    @ExampleObject(
                        name = "ValidationErrorResponse",
                        summary = "Ошибки валидации",
                        value = """
                        {
                            "status": 400,
                            "errorCode": "INVALID_DATA",
                            "message": "Invalid data",
                            "errors": {
                                "name": ["Job name can't be null"]
                            }
                        }
                        """
                    )
                })),
            @ApiResponse(responseCode = "404", description = "Переданные Тэги/Категория не существуют",
                content = @Content(schema = @Schema(implementation = ApiErrorResponse.class),
                examples = {
                        @ExampleObject(
                                name = "TagsNotFound",
                                summary = "Тэги/Категория не найдены",
                                value = """
                                {
                                    "status": 404,
                                    "errorCode": "TAGS_NOT_FOUND",
                                    "message": "Given tags ['Tag1', 'Tag2'] are absent",
                                    "errors": null
                                }
                                """
                        )
                }
                )),
            @ApiResponse(responseCode = "409", description = "Услуга уже существует",
                content = @Content(schema = @Schema(implementation = ApiErrorResponse.class),
                examples = {
                        @ExampleObject(
                            name = "JobExists",
                            summary = "Услуга для данного пользователя уже существует",
                            value = """
                            {
                                "status": 409,
                                "errorCode": "JOB_ALREADY_EXISTS",
                                "message": "Job with name Работа already created by user",
                                "errors": null
                            }
                            """
                        )
                }
                ))
    })
    public ResponseEntity<Job> createJob(@RequestBody @Valid CreateJobDto dto,
                                         @AuthenticationPrincipal UserEntity user) {
        log.info("Processing new create job request from user: {}", user.getId());
        Job result = jobService.createJob(dto, user);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение по Id", responses = {
            @ApiResponse(responseCode = "200", description = "Услуга найдена",
                content = @Content(schema = @Schema(implementation = Job.class))),
            @ApiResponse(responseCode = "404", description = "Услуга не найдена",
                content = @Content(schema = @Schema(implementation = ApiErrorResponse.class),
                examples = {
                    @ExampleObject(
                        name = "TagsNotFound",
                        summary = "Тэги/Категория не найдены",
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
                ))
    })
    public ResponseEntity<Job> getJobById(@PathVariable UUID id) {
        log.info("Processing get job request. jobId={}", id);
        Job result = jobService.getJobById(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping
    @Operation(summary = "Обновление услуги", responses = {
            @ApiResponse(responseCode = "200", description = "Услуга создана",
                content = @Content(schema = @Schema(implementation = Job.class))),
            @ApiResponse(responseCode = "400", description = "Ошибки валидации",
                content = @Content(schema = @Schema(implementation = ApiErrorResponse.class),
                examples = {
                    @ExampleObject(
                        name = "ValidationErrorResponse",
                        summary = "Ошибки валидации",
                        value = """
                        {
                            "status": 400,
                            "errorCode": "INVALID_DATA",
                            "message": "Invalid data",
                            "errors": {
                                "name": ["Job name can't be null"]
                            }
                        }
                        """
                    )
                }
                )),
            @ApiResponse(responseCode = "403", description = "Ошибка доступа к услуге",
                content = @Content(schema = @Schema(implementation = ApiErrorResponse.class),
                examples = {
                    @ExampleObject(
                        name = "Forbidden",
                        summary = "Доступ запрещен",
                        value = """
                        {
                            "status": 403,
                            "errorCode": "FORBIDDEN",
                            "message": "Forbidden message",
                            "errors": null
                        }
                        """
                    )
                }
                )),
            @ApiResponse(responseCode = "404", description = "Переданные Тэги/Категория/Услуга не существуют",
                content = @Content(schema = @Schema(implementation = ApiErrorResponse.class),
                examples = {
                        @ExampleObject(
                            name = "TagsNotFound",
                            summary = "Тэги/Категория не найдены",
                            value = """
                            {
                                "status": 404,
                                "errorCode": "TAGS_NOT_FOUND",
                                "message": "Given tags ['Tag1', 'Tag2'] are absent",
                                "errors": null
                            }
                            """
                        )
                }
            )),
            @ApiResponse(responseCode = "409", description = "Услуга уже существует",
                content = @Content(schema = @Schema(implementation = ApiErrorResponse.class),
                examples = {
                    @ExampleObject(
                            name = "JobExists",
                            summary = "Услуга для данного пользователя уже существует",
                            value = """
                            {
                                "status": 409,
                                "errorCode": "JOB_ALREADY_EXISTS",
                                "message": "Job with name Работа already created by user",
                                "errors": null
                            }
                            """
                    )
                }
            ))
    })
    @PreAuthorize("@jobSecurityService.hasAccess(#dto.id, authentication)")
    public ResponseEntity<Job> updateJob(@RequestBody @Valid UpdateJobDto dto,
                                         @AuthenticationPrincipal UserEntity user) {
        log.info("Processing update job request. jobId={}, userId={}", dto.id(), user.getId());
        Job result = jobService.updateJob(dto, user);
        return ResponseEntity.ok(result);
    }

    @GetMapping
    @Operation(summary = "Получение услуг с фильтрами", responses = {
            @ApiResponse(responseCode = "200", description = "Услуги найдены"),
            @ApiResponse(responseCode = "404", description = "Услуги не найдены",
                content = @Content(schema = @Schema(implementation = ApiErrorResponse.class),
                examples = {
                    @ExampleObject(
                        name = "JobsNotFound",
                        summary = "Услуги не найдены",
                        value = """
                        {
                            "status": 404,
                            "errorCode": "JOBS_NOT_FOUND",
                            "message": "Jobs with given filters are not found",
                            "errors": null
                        }
                        """
                    )
                }
                ))
    })
    public ResponseEntity<Page<Job>> getJobs(
        @ModelAttribute @Valid JobFilters filters,
        @AuthenticationPrincipal UserEntity user
    ) {
        return ResponseEntity
                .ok(jobService.getJobsFiltered(filters, user.getId()));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление услуги", responses = {
            @ApiResponse(responseCode = "200", description = "Услуга удалена"),
            @ApiResponse(responseCode = "403", description = "Ошибки доступа",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = {
                        @ExampleObject(
                            name = "Forbidden",
                            summary = "Доступ запрещен",
                            value = """
                            {
                                "status": 403,
                                "errorCode": "FORBIDDEN",
                                "message": "Forbidden message",
                                "errors": null
                            }
                            """
                        )
                    })),
            @ApiResponse(responseCode = "404", description = "Услуга не найдена",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class),
                        examples = {
                            @ExampleObject(
                                name = "JobNotFound",
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
    })
    @PreAuthorize("@jobSecurityService.hasAccess(#id, authentication)")
    public ResponseEntity<Void> deleteJob(@PathVariable UUID id,
                                          @AuthenticationPrincipal UserEntity currentUser) {
        log.info("Processing delete job request for job id: {} from user: {}", id, currentUser.getId());
        jobService.deleteJob(id, currentUser);

        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/cover", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @Operation(summary = "Обновление обложки услуги", responses = {
            @ApiResponse(responseCode = "200", description = "Обложка обновлена"),
            @ApiResponse(responseCode = "400", description = "Не удалось обновить обложку",
                content = @Content(schema = @Schema(implementation = ApiErrorResponse.class),
                examples = {
                    @ExampleObject(
                        name = "UnableToUpdateCover",
                        summary = "Не удалось обновить обложку",
                        value = """
                                {
                                    "status": 400,
                                    "errorCode": "UNABLE_TO_UPDATE_COVER",
                                    "message": "Unable to update cover for job: 000-00",
                                    "errors": null
                                }
                                """
                    )
                })
            ),
            @ApiResponse(responseCode = "403", description = "Ошибки доступа",
                content = @Content(schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = {
                        @ExampleObject(
                                name = "Forbidden",
                                summary = "Доступ запрещен",
                                value = """
                                        {
                                            "status": 403,
                                            "errorCode": "FORBIDDEN",
                                            "message": "Forbidden message",
                                            "errors": null
                                        }
                                        """
                        )
                    }
                )
            ),
            @ApiResponse(responseCode = "404", description = "Услуга не найдена",
                content = @Content(schema = @Schema(implementation = ApiErrorResponse.class),
                    examples = {
                        @ExampleObject(
                            name = "JobNotFound",
                            summary = "Тэги/Категория не найдены",
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
                )
            ),
    })
    @PreAuthorize("@jobSecurityService.hasAccess(#jobId, authentication)")
    public ResponseEntity<CoverUploadResponse> updateCover(
        @RequestPart("file") MultipartFile file,
        @RequestPart("id") UUID jobId,
        @AuthenticationPrincipal UserEntity currentUser
    ) {
        log.info("Processing update cover for job {} by user {}", jobId, currentUser.getId());

        return ResponseEntity.ok(jobService.updateCover(jobId, file));
    }
}
