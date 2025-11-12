package com.vpr42.marketplacefeedapi.model.dto;

import java.util.Set;

/**
 * Dto с информацией о мастере
 */
public record Master(
    Integer experience,
    String description,
    String pseudonym,
    String phoneNumber,
    String workingHours,
    Set<String> skills
) { }
