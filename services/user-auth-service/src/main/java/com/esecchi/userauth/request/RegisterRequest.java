package com.esecchi.userauth.request;

import com.esecchi.userauth.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterRequest(
    @NotBlank(message = "El nombre es obligatorio")
    String firstName,

    @NotBlank(message = "El apellido es obligatorio")
    String lastName,

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe ser de un formato válido")
    String email,

    @NotBlank(message = "La contraseña es obligatoria")
    String password,

    @NotNull(message = "El rol es obligatorio")
    Role role
) {}