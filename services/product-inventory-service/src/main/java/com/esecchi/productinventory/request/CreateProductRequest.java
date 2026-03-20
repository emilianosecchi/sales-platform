package com.esecchi.productinventory.request;

import com.esecchi.productinventory.model.ProductCategory;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateProductRequest(
        @NotBlank(message = "El nombre del producto es obligatorio")
        String name,

        String description,

        @Min(0)
        @NotNull
        BigDecimal price,

        @NotNull(message = "La categoria del producto es obligatoria")
        ProductCategory category
) {}
