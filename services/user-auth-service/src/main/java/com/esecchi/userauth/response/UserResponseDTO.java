package com.esecchi.userauth.response;

import java.time.LocalDate;

public record UserResponseDTO(
    Long id,
    String email,
    String firstName,
    String lastName,
    LocalDate registrationDate
) {}
