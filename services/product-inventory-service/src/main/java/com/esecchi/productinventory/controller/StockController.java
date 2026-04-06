package com.esecchi.productinventory.controller;

import com.esecchi.productinventory.response.StockResponseDTO;
import com.esecchi.productinventory.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stock")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StockResponseDTO>> getStock(
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Long warehouseId
    ) {
        return ResponseEntity.ok(
                stockService.getStockFilteredBy(productId, warehouseId)
        );
    }

}
