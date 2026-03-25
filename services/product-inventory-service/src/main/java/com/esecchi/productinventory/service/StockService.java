package com.esecchi.productinventory.service;

import com.esecchi.common.dto.order.OrderItemDTO;
import com.esecchi.productinventory.exception.InsufficientStockException;
import com.esecchi.productinventory.exception.ProductNotFoundException;
import com.esecchi.productinventory.exception.WarehouseNotFoundException;
import com.esecchi.productinventory.model.Stock;
import com.esecchi.productinventory.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
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
    public void reserveStock(List<OrderItemDTO> items) throws InsufficientStockException {
        for (OrderItemDTO item : items) {
            Stock stock = stockRepository.findFirstByProduct_IdAndQuantityGreaterThanEqualOrderByQuantityDesc(item.productId(), item.quantity())
                    .orElseThrow(() -> new InsufficientStockException("No hay disponibilidad del producto con id: " + item.productId()));
            stock.setQuantity(stock.getQuantity() - item.quantity());
            stockRepository.save(stock);
            // TODO: Sería necesario guardar el id del Stock que fue modificado en el caso de tener que realizar un rollback por un fallo en el resto de la transacción distribuida
        }
    }

}
