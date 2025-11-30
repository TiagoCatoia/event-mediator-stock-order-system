package br.ifsp.stock_order.product.application;

import br.ifsp.stock_order.product.api.dto.StockResponse;
import br.ifsp.stock_order.product.infrastructure.StockEntity;
import br.ifsp.stock_order.product.infrastructure.StockRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {
    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public List<StockResponse> findAllStocks() {
        List<StockEntity> stocks = stockRepository.findAll();

        return stocks.stream().map(s -> new StockResponse(
                s.getId(),
                s.getProduct().getId(),
                s.getQuantity(),
                s.getUpdatedAt()
        )).toList();
    }
}
