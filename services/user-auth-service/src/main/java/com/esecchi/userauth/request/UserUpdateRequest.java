package com.esecchi.userauth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        String firstName,
        String lastName,
        @Email(message = "El email debe ser de un formato válido")
        String email,
        @Size(min = 4, message = "La contraseña debe tener al menos 4 caracteres")
        String password
) {}
