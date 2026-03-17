package com.esecchi.common.event.user;

import java.time.LocalDate;

public record UserCreatedEvent(
        Long userId,
        String email,
        String firstName,
        String lastName,
        String role,
        LocalDate registrationDate
) {}