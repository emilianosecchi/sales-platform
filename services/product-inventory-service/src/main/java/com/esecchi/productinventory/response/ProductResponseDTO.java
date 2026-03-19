package com.esecchi.productinventory.response;

import com.esecchi.productinventory.model.ProductCategory;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProductResponseDTO(
   Long id,
   String name,
   String description,
   BigDecimal price,
   LocalDate creationDate,
   ProductCategory category
) {}
