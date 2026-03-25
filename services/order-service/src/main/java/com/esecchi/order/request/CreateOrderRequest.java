package com.esecchi.order.request;

import com.esecchi.common.dto.product.ProductItem;
import com.esecchi.order.model.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateOrderRequest(
        @NotEmpty(message = "La lista de productos no puede estar vacía")
        @Valid
        List<ProductItem> items,
        @NotBlank
        PaymentMethod paymentMethod,
        @NotBlank
        String paymentToken
) {}
