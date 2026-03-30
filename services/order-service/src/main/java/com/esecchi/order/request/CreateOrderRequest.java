package com.esecchi.order.request;

import com.esecchi.common.model.order.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateOrderRequest(
        @NotEmpty(message = "La lista de productos no puede estar vacía")
        @Valid
        List<ItemRequest> items,
        @NotNull
        PaymentMethod paymentMethod,
        @NotBlank
        String paymentToken
) {}
