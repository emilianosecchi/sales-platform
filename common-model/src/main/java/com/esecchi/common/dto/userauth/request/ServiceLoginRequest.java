package com.esecchi.common.dto.userauth.request;

import jakarta.validation.constraints.NotBlank;

public record ServiceLoginRequest(
        @NotBlank
        String clientId,
        @NotBlank
        String clientSecret
) {}
