package com.esecchi.order.controller;

import com.esecchi.order.request.CreateOrderRequest;
import com.esecchi.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(name = "/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PostMapping
    public ResponseEntity<Object> createOrder(@RequestBody @Valid CreateOrderRequest request, Authentication authentication) {
        return ResponseEntity.ok(
                orderService.createOrder(request, Long.parseLong(authentication.getName()))
        );
    }


}
