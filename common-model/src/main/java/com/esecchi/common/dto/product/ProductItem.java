package com.esecchi.common.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ProductItem(
        @NotNull(message = "El id del producto es obligatorio")
        Long productId,
        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad seleccionada debe ser al menos 1")
        Integer quantity
) {}
