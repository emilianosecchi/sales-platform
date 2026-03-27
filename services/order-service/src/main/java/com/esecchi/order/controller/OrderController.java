package com.esecchi.order.controller;

import com.esecchi.order.request.CreateOrderRequest;
import com.esecchi.order.response.OrderResponse;
import com.esecchi.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{id}")
    // Si el usuario que quiere consultar la orden no es propietario de la misma, se aplica el filtro de autorización
    @PostAuthorize("hasRole('ADMIN') or returnObject.userId.toString() == authentication.name")
    public OrderResponse getOrder(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @PostMapping
    public ResponseEntity<Object> createOrder(@RequestBody @Valid CreateOrderRequest request, Authentication authentication) {
        return ResponseEntity.ok(
                orderService.createOrder(request, Long.parseLong(authentication.getName()))
        );
    }


}
