package com.esecchi.productinventory.service;

import com.esecchi.common.dto.order.OrderItemDTO;
import com.esecchi.productinventory.exception.InsufficientStockException;
import com.esecchi.productinventory.exception.StockNotFoundException;
import com.esecchi.productinventory.model.Stock;
import com.esecchi.productinventory.model.StockReservation;
import com.esecchi.productinventory.repository.StockRepository;
import com.esecchi.productinventory.repository.StockReservationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockServiceTest {

    @Mock
    private StockRepository stockRepository;
    @Mock
    private StockReservationRepository stockReservationRepository;
    @InjectMocks
    private StockService stockService;

    @Test
    public void reserveStock_Success() {
        // Arrange
        Long orderId = 1L;
        OrderItemDTO item1 = new OrderItemDTO(1L, 5, BigDecimal.valueOf(150));
        OrderItemDTO item2 = new OrderItemDTO(2L, 2, BigDecimal.valueOf(350));
        List<OrderItemDTO> items = List.of(item1, item2);

        Stock stock1 = Stock.builder().id(10L).quantity(5).build();
        Stock stock2 = Stock.builder().id(20L).quantity(10).build();

        when(stockRepository.findFirstByProduct_IdAndQuantityGreaterThanEqualOrderByQuantityDesc(1L, 5))
                .thenReturn(Optional.of(stock1));
        when(stockRepository.findFirstByProduct_IdAndQuantityGreaterThanEqualOrderByQuantityDesc(2L, 2))
                .thenReturn(Optional.of(stock2));

        // Act
        stockService.reserveStock(orderId, items);

        // Assert
        // Se verifica que se haya descontado correctamente la cantidad ordenada del stock correspondiente.
        assertEquals(0, stock1.getQuantity());
        assertEquals(8, stock2.getQuantity());
        // Verificamos que se llamó al save del stock 2 veces
        verify(stockRepository, times(2)).save(any(Stock.class));
        // Verificamos que se crearon y guardaron 2 reservas de stock
        verify(stockReservationRepository, times(2)).save(any(StockReservation.class));
    }

    @Test
    public void reserveStock_InsufficientStock_ThrowsException() {
        // Arrange
        Long orderId = 100L;
        OrderItemDTO item = new OrderItemDTO(1L, 50, BigDecimal.valueOf(350));
        List<OrderItemDTO> items = List.of(item);

        when(stockRepository.findFirstByProduct_IdAndQuantityGreaterThanEqualOrderByQuantityDesc(1L, 50))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(InsufficientStockException.class, () -> stockService.reserveStock(orderId, items));
        verify(stockRepository, never()).save(any());
        verify(stockReservationRepository, never()).save(any());
    }

    @Test
    void releaseStockReserved_Success() {
        // Arrange
        Long orderId = 1L;

        // Simulamos dos reservas para la misma orden
        StockReservation res1 = StockReservation.builder()
                .stockId(101L).quantityReserved(10).orderId(orderId).build();
        StockReservation res2 = StockReservation.builder()
                .stockId(102L).quantityReserved(5).orderId(orderId).build();

        List<StockReservation> reservations = List.of(res1, res2);

        when(stockReservationRepository.findByOrderId(orderId)).thenReturn(reservations);

        // Simulamos los stocks actuales en la DB
        Stock stock1 = Stock.builder().id(101L).quantity(50).build();
        Stock stock2 = Stock.builder().id(102L).quantity(20).build();

        when(stockRepository.findById(101L)).thenReturn(Optional.of(stock1));
        when(stockRepository.findById(102L)).thenReturn(Optional.of(stock2));

        // Act
        stockService.releaseStockReserved(orderId);

        // Assert
        // Verificamos que las cantidades se sumaron correctamente
        assertThat(stock1.getQuantity()).isEqualTo(60);
        assertThat(stock2.getQuantity()).isEqualTo(25);

        // Verificamos interacciones con los repositorios
        verify(stockRepository, times(2)).save(any(Stock.class));
        verify(stockReservationRepository).deleteAll(reservations);
    }

    @Test
    public void releaseStockReserved_StockNotFound_ThrowsException() {
        // Arrange
        Long orderId = 1L;
        StockReservation res1 = StockReservation.builder().stockId(10L).quantityReserved(5).build();
        StockReservation res2 = StockReservation.builder().stockId(20L).quantityReserved(5).build();
        when(stockReservationRepository.findByOrderId(orderId)).thenReturn(List.of(res1, res2));

        // Para la primera reserva el stock existe
        Stock stock1 = Stock.builder().id(10L).quantity(10).build();
        when(stockRepository.findById(10L)).thenReturn(Optional.of(stock1));

        // Para la segunda reserva el stock no existe
        when(stockRepository.findById(20L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(StockNotFoundException.class, () -> stockService.releaseStockReserved(orderId));
        verify(stockRepository, times(1)).save(stock1);
        verify(stockReservationRepository, never()).deleteAll(any());
    }

}
