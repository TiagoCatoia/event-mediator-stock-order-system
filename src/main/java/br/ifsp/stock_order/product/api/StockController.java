package br.ifsp.stock_order.product.api;

import br.ifsp.stock_order.product.api.dto.CreateStockRequest;
import br.ifsp.stock_order.product.api.dto.StockResponse;
import br.ifsp.stock_order.product.application.StockService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("stocks")
@Tag(name = "Stock")
public class StockController {
    private final StockService stockService;

    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping
    public ResponseEntity<List<StockResponse>> findAllStocks() {
        return ResponseEntity.ok(stockService.findAllStocks());
    }

    @PostMapping
    public ResponseEntity<StockResponse> createStock(@RequestBody @Valid CreateStockRequest request) {
        return ResponseEntity.ok(stockService.createStock(request));
    }
}
