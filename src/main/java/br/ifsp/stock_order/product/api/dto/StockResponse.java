package br.ifsp.stock_order.product.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record StockResponse(
        UUID id,
        UUID productId,
        Integer quantity,
        LocalDateTime updatedAt
) {
}
