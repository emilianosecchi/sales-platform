package com.esecchi.common.event.user;

public record UserMailUpdatedEvent(
        Long userId,
        String newEmail
) {
}
