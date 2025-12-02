package br.ifsp.stock_order.product.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PathStockRequest(
        @NotNull(message = "Quantity is required")
        @Min(value = 0, message = "Quantity must be zero or positive")
        Integer quantity
) {
}
