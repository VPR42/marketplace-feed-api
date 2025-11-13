package com.vpr42.marketplacefeedapi.model.entity.keys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Embeddable
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FavouriteKey {
    @Column(name = "user_id")
    UUID userId;
    @Column(name = "job_id")
    UUID jobId;
}
