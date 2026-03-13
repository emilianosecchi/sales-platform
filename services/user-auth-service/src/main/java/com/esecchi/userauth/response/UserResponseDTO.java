package com.esecchi.userauth.response;

import com.esecchi.userauth.model.Role;

import java.time.LocalDate;

public record UserResponseDTO(
    Long id,
    String email,
    String firstName,
    String lastName,
    LocalDate registrationDate,
    Role role
) {}
