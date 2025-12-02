package br.ifsp.stock_order.product.application;

import br.ifsp.stock_order.common.event.OrderCreatedEvent;
import br.ifsp.stock_order.product.api.dto.CreateStockRequest;
import br.ifsp.stock_order.product.api.dto.PathStockRequest;
import br.ifsp.stock_order.product.api.dto.StockResponse;
import br.ifsp.stock_order.product.infrastructure.ProductEntity;
import br.ifsp.stock_order.product.infrastructure.ProductRepository;
import br.ifsp.stock_order.product.infrastructure.StockEntity;
import br.ifsp.stock_order.product.infrastructure.StockRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StockService {
    private final StockRepository stockRepository;
    private final ProductRepository productRepository;

    public StockService(StockRepository stockRepository, ProductRepository productRepository) {
        this.stockRepository = stockRepository;
        this.productRepository = productRepository;
    }

    public List<StockResponse> findAllStocks() {
        List<StockEntity> stocks = stockRepository.findAll();

        return stocks.stream().map(s -> new StockResponse(
                s.getId(),
                s.getProduct().getId(),
                s.getProduct().getName(),
                s.getQuantity(),
                s.getUpdatedAt()
        )).toList();
    }

    public StockResponse createStock(CreateStockRequest request) {
        ProductEntity product = productRepository.findById(request.productId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + request.productId()));

         Optional<StockEntity> existingStock = stockRepository.findByProductId(product.getId());
         if (existingStock.isPresent()) {
            throw new EntityExistsException("Stock already exists for product: " + product.getId());
         }

        StockEntity stock = new StockEntity(
                request.quantity(),
                product
        );

        stockRepository.save(stock);

        return new StockResponse(
                stock.getId(),
                stock.getProduct().getId(),
                stock.getProduct().getName(),
                stock.getQuantity(),
                stock.getUpdatedAt()
        );
    }

    public void reserveStock(UUID orderId, List<OrderCreatedEvent.OrderItemData> items) {
        for (var item : items){
            StockEntity stock = stockRepository.findByProductId(item.productId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found: " + item.productId()));

            if (stock.getQuantity() < item.quantity()) {
                throw new IllegalArgumentException("Quantity exceeds stock quantity for " + item.productId());
            }

            stock.setQuantity(stock.getQuantity() - item.quantity());
            stock.setUpdatedAt(LocalDateTime.now());

            stockRepository.save(stock);
        }
    }

    public StockResponse pathStock(UUID stockId, PathStockRequest request) {
        StockEntity stock = stockRepository.findById(stockId)
                .orElseThrow(() -> new EntityNotFoundException("Stock not found: " + stockId));

        stock.setQuantity(request.quantity());

        stockRepository.save(stock);

        return new StockResponse(
                stock.getId(),
                stock.getProduct().getId(),
                stock.getProduct().getName(),
                stock.getQuantity(),
                stock.getUpdatedAt()
        );
    }
}
