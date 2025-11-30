package br.ifsp.stock_order.order.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record CreateOrderItemRequest(
        @NotNull(message = "Product ID is required")
        UUID productId,

        @Positive(message = "Quantity must be greater than zero")
        Integer quantity
) {}