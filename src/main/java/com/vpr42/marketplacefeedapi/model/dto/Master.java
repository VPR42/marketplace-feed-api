package com.vpr42.marketplacefeedapi.model.dto;

import java.util.List;

public record Master(
    Integer experience,
    String description,
    String pseudonym,
    String phoneNumber,
    String workingHours,
    List<String> skills
) { }
