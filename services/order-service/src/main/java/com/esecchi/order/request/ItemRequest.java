package com.esecchi.order.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ItemRequest(
        @NotNull(message = "El id del producto es obligatorio")
        Long productId,
        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad seleccionada debe ser al menos 1")
        Integer quantity
) {}
