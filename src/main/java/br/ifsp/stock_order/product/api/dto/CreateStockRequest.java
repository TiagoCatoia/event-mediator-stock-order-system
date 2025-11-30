package br.ifsp.stock_order.product.api.dto;

import java.util.UUID;

public record CreateStockRequest(
        UUID productId,
        Integer quantity
) {
}
