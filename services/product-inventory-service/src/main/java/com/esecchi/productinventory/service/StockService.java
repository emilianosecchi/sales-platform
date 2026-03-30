package com.esecchi.productinventory.service;

import com.esecchi.common.dto.order.OrderItemDTO;
import com.esecchi.productinventory.exception.InsufficientStockException;
import com.esecchi.productinventory.exception.ProductNotFoundException;
import com.esecchi.productinventory.exception.StockNotFoundException;
import com.esecchi.productinventory.exception.WarehouseNotFoundException;
import com.esecchi.productinventory.model.Stock;
import com.esecchi.productinventory.model.StockReservation;
import com.esecchi.productinventory.repository.StockRepository;
import com.esecchi.productinventory.repository.StockReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final StockReservationRepository stockReservationRepository;

    private final ProductService productService;
    private final WarehouseService warehouseService;

    @Transactional
    public Boolean addStock(Long productId, Long warehouseId, Integer quantity) {
        if (!productService.existsById(productId))
            throw new ProductNotFoundException(productId);

        if (!warehouseService.existsById(warehouseId))
            throw new WarehouseNotFoundException(warehouseId);

        Optional<Stock> stock = stockRepository.findByProduct_IdAndWarehouse_Id(productId, warehouseId);
        if (stock.isPresent()) {
            stock.get().setQuantity(stock.get().getQuantity() + quantity);
        } else {
            Stock newStock = Stock.builder()
                    .quantity(quantity)
                    .product(productService.getProductReferenceById(productId))
                    .warehouse(warehouseService.getWarehouseReferenceById(warehouseId))
                    .build();
            stockRepository.save(newStock);
        }

        return true;
    }

    @Transactional
    public void reserveStock(Long orderId, List<OrderItemDTO> items) throws InsufficientStockException {
        if (stockReservationRepository.existsByOrderId(orderId))
            return;

        for (OrderItemDTO item : items) {
            Stock stock = stockRepository.findFirstByProduct_IdAndQuantityGreaterThanEqualOrderByQuantityDesc(item.productId(), item.quantity())
                    .orElseThrow(() -> new InsufficientStockException("No hay disponibilidad del producto con id: " + item.productId()));
            stock.setQuantity(stock.getQuantity() - item.quantity());
            stockRepository.save(stock);
            StockReservation stockReservation = StockReservation.builder()
                    .stockId(stock.getId())
                    .quantityReserved(item.quantity())
                    .orderId(orderId)
                    .build();
            stockReservationRepository.save(stockReservation);
        }
    }

    @Transactional
    public void releaseStockReserved(Long orderId) {
        List<StockReservation> reservedStock = stockReservationRepository.findByOrderId(orderId);
        if (reservedStock.isEmpty()) {
            return;
        }
        for (StockReservation stockReservation : reservedStock) {
            Stock stock = stockRepository.findById(stockReservation.getStockId()).orElseThrow(() -> new StockNotFoundException(stockReservation.getStockId()));
            stock.setQuantity(stock.getQuantity() + stockReservation.getQuantityReserved());
            stockRepository.save(stock);
        }
        stockReservationRepository.deleteAll(reservedStock);
    }

}
