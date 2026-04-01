package com.esecchi.productinventory.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdateProductPriceRequest(
        @Positive(message = "El precio del producto debe ser mayor a cero")
        @NotNull(message = "El precio del producto es obligatorio")
        BigDecimal price
) {}